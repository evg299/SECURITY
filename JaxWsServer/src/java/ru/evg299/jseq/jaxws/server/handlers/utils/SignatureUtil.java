package ru.evg299.jseq.jaxws.server.handlers.utils;

/**
 * Created by Evgeny(e299792459@gmail.com) on 16.02.14.
 */

import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.token.X509Security;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.CryptoPro.JCP.JCP;
import ru.CryptoPro.JCPxml.xmldsig.JCPXMLDSigInit;

import javax.xml.crypto.KeySelector;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SignatureUtil {

    public static void signSOAP(SOAPMessage message, String certID, String containerName, String containerCode) throws Exception {
        /*** Инициализация ***/

        // Инициализация Transforms алгоритмов.
        com.sun.org.apache.xml.internal.security.Init.init();

        // Инициализация JCP XML провайдера.
        if (!JCPXMLDSigInit.isInitialized()) {
            JCPXMLDSigInit.init();
        }

        // Инициализация ключевого контейнера.
        KeyStore keyStore = KeyStore.getInstance(JCP.HD_STORE_NAME);
        keyStore.load(null, containerCode.toCharArray());

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(containerName,
                containerCode.toCharArray());
        // Сертификат открытого ключа
        X509Certificate cert = (X509Certificate) keyStore
                .getCertificate(containerName);

        /*** Подготовка документа ***/
        message.getSOAPPart()
                .getEnvelope()
                .addNamespaceDeclaration("wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        message.getSOAPPart()
                .getEnvelope()
                .addNamespaceDeclaration("wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
        message.getSOAPPart()
                .getEnvelope()
                .addNamespaceDeclaration("ds", "http://www.w3.org/2000/09/xmldsig#");
        message.getSOAPBody()
                .setAttributeNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu:Id", "body");

        // Формируем заголовок.
        WSSecHeader header = new WSSecHeader();
        header.setActor("http://smev.gosuslugi.ru/actors/smev");
        header.setMustUnderstand(false);

        // Получаем документ.
        Document doc = message.getSOAPPart().getEnvelope().getOwnerDocument();
        Element secHeader = header.insertSecurityHeader(message.getSOAPPart()
                .getEnvelope().getOwnerDocument());
        Element token = header.getSecurityHeader();

        NodeList list = secHeader
                .getElementsByTagName("wsse:BinarySecurityToken");
        Element binSecToken = null;
        if (list.getLength() != 0) {
            binSecToken = (Element) list.item(0);
        } else {
            binSecToken = (Element) header
                    .getSecurityHeader()
                    .appendChild(doc.createElementNS("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "wsse:BinarySecurityToken"));
        }

        binSecToken.setAttribute("wsu:Id", certID);
        binSecToken.setAttribute("EncodingType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
        binSecToken.setAttribute("ValueType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");

        /*** Подпись данных ***/

        // Загрузка провайдера.
        Provider xmlDSigProvider = new ru.CryptoPro.JCPxml.dsig.internal.dom.XMLDSigRI();

        // Преобразования над документом.
        final Transforms transforms = new Transforms(doc);
        transforms.addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);

        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",
                xmlDSigProvider);

        // Преобразования над блоком SignedInfo
        List<Transform> transformList = new ArrayList<Transform>();
        Transform transformC14N = fac.newTransform(
                Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS,
                (XMLStructure) null);
        transformList.add(transformC14N);

        // Ссылка на подписываемые данные.
        Reference ref = fac.newReference("#body", fac.newDigestMethod(
                "http://www.w3.org/2001/04/xmldsig-more#gostr3411", null),
                transformList, null, null);

        // Блок SignedInfo.
        SignedInfo si = fac
                .newSignedInfo(
                        fac.newCanonicalizationMethod(
                                CanonicalizationMethod.EXCLUSIVE,
                                (C14NMethodParameterSpec) null),
                        fac.newSignatureMethod("http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411",null),
                        Collections.singletonList(ref));

        // Блок KeyInfo.
        KeyInfoFactory kif = fac.getKeyInfoFactory();
        X509Data x509d = kif.newX509Data(Collections.singletonList(cert));
        KeyInfo ki = kif.newKeyInfo(Collections.singletonList(x509d));

        // Подпись данных.
        javax.xml.crypto.dsig.XMLSignature sig = fac.newXMLSignature(si, ki);
        DOMSignContext signContext = new DOMSignContext(privateKey, token);
        sig.sign(signContext);

        // Блок подписи Signature.
        Element sigE = (Element) XPathAPI.selectSingleNode(signContext.getParent(), "//ds:Signature");
        // Блок данных KeyInfo.
        Node keyE = XPathAPI.selectSingleNode(sigE, "//ds:KeyInfo", sigE);

        // Элемент SenderCertificate, который должен содержать сертификат.
        list = secHeader.getElementsByTagName("wsse:BinarySecurityToken");
        Element cerVal = (Element) list.item(0);

        // Element cerVal = (Element) XPathAPI.selectSingleNode(token,
        // "//*[@wsu:Id='SenderCertificate']");
        cerVal.setTextContent(XPathAPI.selectSingleNode(keyE, "//ds:X509Certificate", keyE).getFirstChild().getNodeValue());

        // Удаляем элементы KeyInfo, попавшие в тело документа. Они должны быть
        // только в header.
        keyE.removeChild(XPathAPI.selectSingleNode(keyE, "//ds:X509Data", keyE));

        NodeList chl = keyE.getChildNodes();

        for (int i = 0; i < chl.getLength(); i++) {
            keyE.removeChild(chl.item(i));
        }

        // Блок KeyInfo содержит указание на проверку подписи с помощью
        // сертификата SenderCertificate.
        Node str = keyE
                .appendChild(doc
                        .createElementNS(
                                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
                                "wsse:SecurityTokenReference"));
        Element strRef = (Element) str
                .appendChild(doc
                        .createElementNS(
                                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
                                "wsse:Reference"));

        strRef.setAttribute(
                "ValueType",
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");
        strRef.setAttribute("URI", "#" + certID);
        header.getSecurityHeader().appendChild(sigE);
    }

    public static boolean checkSOAP(SOAPMessage message, boolean printCert)
            throws Exception {
        boolean rez = false;

        // Global init
        if (!com.sun.org.apache.xml.internal.security.Init.isInitialized())
            com.sun.org.apache.xml.internal.security.Init.init();

        if (!JCPXMLDSigInit.isInitialized())
            JCPXMLDSigInit.init();

        System.setProperty("com.ibm.security.enableCRLDP", "false");
        // Add namespace declaration of the wsse
        message.getSOAPPart()
                .getEnvelope()
                .addNamespaceDeclaration("wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");

        // Получение блока, содержащего сертификат.
        Document doc = message.getSOAPPart().getEnvelope().getOwnerDocument();
        final Element wssecontext = doc.createElementNS(null, "namespaceContext");

        wssecontext.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        NodeList secnodeList = XPathAPI.selectNodeList(
                doc.getDocumentElement(), "//wsse:Security");

        // Поиск элемента сертификата.
        Element r = null;
        Element el = null;
        if (secnodeList != null && secnodeList.getLength() > 0) {
            String actorAttr = null;

            for (int i = 0; i < secnodeList.getLength(); i++) {
                el = (Element) secnodeList.item(i);
                actorAttr = el.getAttributeNS(
                        "http://schemas.xmlsoap.org/soap/envelope/", "actor");

                if (actorAttr != null
                        && actorAttr
                        .equals("http://smev.gosuslugi.ru/actors/smev")) {
                    r = (Element) XPathAPI.selectSingleNode(el,
                            "//wsse:BinarySecurityToken[1]", wssecontext);
                    break;
                }
            }
        }

        if (r == null) {
            return false;
        }

        // Получение сертификата.
        final X509Security x509 = new X509Security(r);

        X509Certificate cert = (X509Certificate) CertificateFactory
                .getInstance("X.509").generateCertificate(
                        new ByteArrayInputStream(x509.getToken()));

        if (cert == null) {
            throw new RuntimeException("Сертификат не найден.");
        } else {
            if (printCert)
                System.out.println("Имя сертификата: " + cert.getSubjectDN());
        }

        // Поиск элемента с подписью.
        NodeList nl = doc.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature");
        if (nl.getLength() == 0) {
            throw new Exception("Не найден элемент Signature.");
        }

        Provider xmlDSigProvider = new ru.CryptoPro.JCPxml.dsig.internal.dom.XMLDSigRI();

        // Задаем открытый ключ для проверки подписи.
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM", xmlDSigProvider);
        DOMValidateContext valContext = new DOMValidateContext(
                KeySelector.singletonKeySelector(cert.getPublicKey()),
                nl.item(0));
        javax.xml.crypto.dsig.XMLSignature signature = fac
                .unmarshalXMLSignature(valContext);

        // Проверяем подпись.
        rez = signature.validate(valContext);
        return rez;
    }
}
