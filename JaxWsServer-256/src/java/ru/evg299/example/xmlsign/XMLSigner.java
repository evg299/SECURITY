package ru.evg299.example.xmlsign;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLSigner {

	public static Element signXML(Document document, String contName,
			String contPass) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("HDImageStore", "JCP");
		keyStore.load(null, null);
		// Извлекаем закрытый ключ
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(contName, contPass.toCharArray());
		// Сертификат открытого ключа
		X509Certificate cert = (X509Certificate) keyStore.getCertificate(contName);

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
		// заданными в узле
		// <ds:Transforms> правилами и заданным алгоритмом хеширования)
		sig.addDocument("", transforms,
				"http://www.w3.org/2001/04/xmldsig-more#gostr3411");

		// создание внутри узла подписи узла <ds:KeyInfo> информации об открытом
		// ключе на основе сертификата
		sig.addKeyInfo(cert);

		// создание подписи XML-документа
		sig.sign(privateKey);

		// Полученая подпись в soap:message
		Element signatureElement = sig.getElement();
		return signatureElement;
	}
}
