package ru.evg299.jseq.xmlseq.cryptopro;

/**
 * Created by Evgeny(e299792459@gmail.com) on 13.02.14.
 */

import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class XmlDeffender {

    public static Document signDocument(Document document, String keyName,
                                        String keyPin) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("HDImageStore", "JCP");
        keyStore.load(null, null);
        // Извлекаем закрытый ключ
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyName,
                keyPin.toCharArray());
        // Сертификат открытого ключа
        X509Certificate cert = (X509Certificate) keyStore
                .getCertificate(keyName);

        ru.CryptoPro.JCPxml.xmldsig.JCPXMLDSigInit.init();

        final XMLSignature sig = new XMLSignature(document, "",
                "http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411");

        // создание узла преобразований <ds:Transforms> обрабатываемого
        // XML-документа
        final Transforms transforms = new Transforms(document);

        // добавление в узел преобразований правил работы с документом
        transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
        transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);

        // добавление в узел подписи ссылок (узла <ds:Reference>), определяющих
        // правила работы с XML-документом (обрабатывается текущий документ с
        // заданными в узле <ds:Transforms> правилами и заданным алгоритмом
        // хеширования)
        sig.addDocument("", transforms,
                "http://www.w3.org/2001/04/xmldsig-more#gostr3411");

        // создание внутри узла подписи узла <ds:KeyInfo> информации об открытом
        // ключе на основе сертификата
        sig.addKeyInfo(cert);

        // создание подписи XML-документа
        sig.sign(privateKey);

        Element signatureElement = sig.getElement();
        document.getFirstChild().appendChild(signatureElement);

        return document;
    }

    public static boolean verDocument(Document document) throws Exception {
        /* Чтение узла подписи <ds:Signature> из XML-документа */

        // чтение из загруженного документа содержимого пространства имени
        // Signature
        final Element nscontext = document.createElementNS(null,
                "namespaceContext");
        nscontext.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:ds",
                Constants.SignatureSpecNS);

        // выбор из прочитанного содержимого пространства имени узла подписи
        // <ds:Signature>
        final Element signatureElement = (Element) XPathAPI.selectSingleNode(
                document, "//ds:Signature[1]", nscontext);

        ru.CryptoPro.JCPxml.xmldsig.JCPXMLDSigInit.init();

        // инициализация объекта проверки подписи
        final XMLSignature signature = new XMLSignature(signatureElement, "");

        /**
         * Надо удалять элемент подписи, тк подписывали документ без элемента
         * подписи
         */
        document.getDocumentElement().removeChild(signatureElement);

        // чтение узла <ds:KeyInfo> информации об открытом ключе
        final KeyInfo ki = signature.getKeyInfo();

        // чтение сертификата их узла информации об открытом ключе
        final X509Certificate certKey = ki.getX509Certificate();

        // если сертификат найден, то осуществляется проверка
        // подписи на основе сертфиката
        if (certKey != null) {
            return signature.checkSignatureValue(certKey);
        } else {
            // чтение открытого ключа из узла информации об открытом ключе
            final PublicKey pk = ki.getPublicKey();
            if (pk != null) {
                return signature.checkSignatureValue(pk);
            }
        }

        return false;
    }
}
