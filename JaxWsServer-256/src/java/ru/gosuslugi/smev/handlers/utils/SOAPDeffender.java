package ru.gosuslugi.smev.handlers.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.crypto.KeySelector;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.token.X509Security;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.CryptoPro.JCPxml.dsig.internal.dom.XMLDSigRI;

public class SOAPDeffender {

	public static void signMessage(SOAPMessage message, String containerName,
			String containerPass) throws SOAPException, WSSecurityException,
			NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			KeyStoreException, NoSuchProviderException, CertificateException,
			IOException, UnrecoverableKeyException, MarshalException,
			XMLSignatureException, TransformerException {

		SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
		SOAPBody body = message.getSOAPBody();

		envelope.addNamespaceDeclaration(
				"wsse",
				"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
		envelope.addNamespaceDeclaration(
				"wsu",
				"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
		envelope.addNamespaceDeclaration("ds",
				"http://www.w3.org/2000/09/xmldsig#");
		body.setAttributeNS(
				"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd",
				"wsu:Id", "body");

		WSSecHeader header = new WSSecHeader();
		header.setActor("http://smev.gosuslugi.ru/actors/smev");
		header.setMustUnderstand(false);

		Element sec = header.insertSecurityHeader(message.getSOAPPart());
		Document doc = envelope.getOwnerDocument();

		Element token = (Element) sec
				.appendChild(doc
						.createElementNS(
								"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
								"wsse:BinarySecurityToken"));
		token.setAttribute(
				"EncodingType",
				"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
		token.setAttribute(
				"ValueType",
				"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");

		NodeList list = sec.getElementsByTagName("wsse:BinarySecurityToken");

		header.getSecurityHeader().appendChild(token);

		Provider xmlDSigProvider = new XMLDSigRI(); // Security.getProvider("JCP");
		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",
				xmlDSigProvider);

		List<Transform> transformList = new ArrayList<Transform>();
		Transform transform = fac.newTransform(Transform.ENVELOPED,
				(XMLStructure) null);
		Transform transformC14N = fac.newTransform(
				Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS,
				(XMLStructure) null);
		transformList.add(transform);
		transformList.add(transformC14N);

		Reference ref = fac.newReference("#body", fac.newDigestMethod(
				"http://www.w3.org/2001/04/xmldsig-more#gostr3411", null),
				transformList, null, null);

		// Указываем методы канонизации и подписания
		SignedInfo si = fac
				.newSignedInfo(
						fac.newCanonicalizationMethod(
								CanonicalizationMethod.EXCLUSIVE,
								(C14NMethodParameterSpec) null),
						fac.newSignatureMethod(
								"http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411",
								null), Collections.singletonList(ref));

		final KeyStore ks = KeyStore.getInstance("HDImageStore", "JCP");
		ks.load(null, null);
		final X509Certificate certX = (X509Certificate) ks
				.getCertificate(containerName);
		final PrivateKey privateKey = (PrivateKey) ks.getKey(containerName,
				containerPass.toCharArray());

		// Id сертификата
		String certID = "CertID-" + certX.getSigAlgOID();
		token.setAttribute("wsu:Id", certID);

		// Добавляем информацию о ключе (открытый ключ) для проверки на другой
		// стороне
		KeyInfoFactory kif = fac.getKeyInfoFactory();
		X509Data x509d = kif.newX509Data(Collections.singletonList(certX));
		KeyInfo ki = kif.newKeyInfo(Collections.singletonList(x509d));

		// Подписываем.
		XMLSignature xmlSignature = fac.newXMLSignature(si, ki);
		DOMSignContext signContext = new DOMSignContext(privateKey, token);
		signContext.putNamespacePrefix(
				javax.xml.crypto.dsig.XMLSignature.XMLNS, "ds");
		xmlSignature.sign(signContext);

		// После того как сообщение было подписан, в нем появились
		// дополнительные элементы.
		// Нам необходимо добраться до узла X509Certificate, взять его значение
		// и поместить в узел BinarySecurityToken
		// После этого узел X509Certificate должен быть удален из узла KeyInfo.

		// Блок подписи Signature.
		Element sigE = (Element) XPathAPI.selectSingleNode(
				signContext.getParent(), "//ds:Signature");
		// Блок данных KeyInfo.
		Node keyE = XPathAPI.selectSingleNode(sigE, "//ds:KeyInfo", sigE);

		// Элемент SenderCertificate, который должен содержать сертификат.
		list = sec.getElementsByTagName("wsse:BinarySecurityToken");
		Element cerVal = (Element) list.item(0);

		cerVal.setTextContent(XPathAPI
				.selectSingleNode(keyE, "//ds:X509Certificate", keyE)
				.getFirstChild().getNodeValue());

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

	public static boolean checkSOAP(SOAPMessage message, boolean printCert) {
		boolean rez = false;
		try {
			System.setProperty("com.ibm.security.enableCRLDP", "false");
			// Add namespace declaration of the wsse
			SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();

			envelope.addNamespaceDeclaration(
					"wsse",
					"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");

			// Получение блока, содержащего сертификат.
			Document doc = envelope.getOwnerDocument();
			final Element wssecontext = doc.createElementNS(null,
					"namespaceContext");

			wssecontext
					.setAttributeNS(
							"http://www.w3.org/2000/xmlns/",
							"xmlns:" + "wsse".trim(),
							"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
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
							"http://schemas.xmlsoap.org/soap/envelope/",
							"actor");

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
				throw new Exception("Сертификат не найден.");
			} else {
				if (printCert)
					System.out.println("Имя сертификата: "
							+ cert.getSubjectDN());
			}

			// Поиск элемента с подписью.
			NodeList nl = doc.getElementsByTagNameNS(
					"http://www.w3.org/2000/09/xmldsig#", "Signature");
			if (nl.getLength() == 0) {
				throw new Exception("Не найден элемент Signature.");
			}

			Provider xmlDSigProvider = new ru.CryptoPro.JCPxml.dsig.internal.dom.XMLDSigRI();

			// Задаем открытый ключ для проверки подписи.
			XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",
					xmlDSigProvider);
			DOMValidateContext valContext = new DOMValidateContext(
					KeySelector.singletonKeySelector(cert.getPublicKey()),
					nl.item(0));
			javax.xml.crypto.dsig.XMLSignature signature = fac
					.unmarshalXMLSignature(valContext);

			// Проверяем подпись.
			rez = signature.validate(valContext);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rez;
	}
}
