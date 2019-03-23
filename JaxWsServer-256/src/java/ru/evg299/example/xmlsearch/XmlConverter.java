package ru.evg299.example.xmlsearch;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlConverter {

	public static DocumentBuilder createNewDocumentBuilder(boolean namespaceAware,
			boolean ignoringElementContentWhitespace) throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(namespaceAware);
		factory.setIgnoringElementContentWhitespace(ignoringElementContentWhitespace);
		return factory.newDocumentBuilder();
	}

	public static Document createDocumentFromString(String xmlString) throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilder documentBuilder = createNewDocumentBuilder(true, true);
		InputSource source = new InputSource(new StringReader(xmlString));
		return documentBuilder.parse(source);
	}

	public static Document createDocumentFromFile(String filePath, String encoding)
			throws ParserConfigurationException, SAXException, IOException {
		String xmlString = FileIO.readFile(filePath, encoding);
		return createDocumentFromString(xmlString);
	}

	public static Document createDocumentFromNode(Node extNode) throws ParserConfigurationException {
		DocumentBuilder documentBuilder = createNewDocumentBuilder(true, true);
		Document document = documentBuilder.newDocument();
		document.appendChild(document.importNode(extNode, true));
		return document;
	}

	public static String xmlNodeToString(Node node, String encoding, boolean addXmlDeclaration, boolean addWhiteSpaces)
			throws TransformerException {
		StringWriter sw = new StringWriter();

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, addXmlDeclaration ? "yes" : "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, addWhiteSpaces ? "yes" : "no");
		transformer.setOutputProperty(OutputKeys.ENCODING, encoding);

		transformer.transform(new DOMSource(node), new StreamResult(sw));
		return sw.toString();
	}

	public static void xmlNodeToFile(Node node, String encoding, String filePath, boolean addXmlDeclaration,
			boolean addWhiteSpaces) throws IOException, TransformerException {
		String nodeString = xmlNodeToString(node, encoding, addXmlDeclaration, addWhiteSpaces);
		FileIO.writeFile(filePath, encoding, nodeString);
	}


}
