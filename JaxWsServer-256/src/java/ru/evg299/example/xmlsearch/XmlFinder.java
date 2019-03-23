package ru.evg299.example.xmlsearch;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlFinder {

	public static Node findFirstByLocalName(Node parent, String localName) {
		if (null == localName || localName.trim().isEmpty())
			return null;

		NodeList nodeList = parent.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);
			if (localName.equals(child.getLocalName()))
				return child;
			else {
				Node subChild = findFirstByLocalName(child, localName);
				if (null != subChild)
					return subChild;
			}
		}

		return null;
	}

	public static Node findFirstByLocalNameAndNS(Node parent, String localName, String namespace) {
		if (null == localName || localName.trim().isEmpty())
			return null;

		NodeList nodeList = parent.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);
			if (localName.equals(child.getLocalName()) && namespace.equals(child.getNamespaceURI())) {
				return child;
			} else {
				Node subChild = findFirstByLocalNameAndNS(child, localName, namespace);
				if (null != subChild)
					return subChild;
			}
		}

		return null;
	}

	public static List<Node> findAllByLocalName(Node parent, String localName) {
		List<Node> nodes = new ArrayList<Node>();
		findAllByLocalNamePriv(nodes, parent, localName);
		return nodes;
	}

	private static void findAllByLocalNamePriv(List<Node> nodes, Node parent, String localName) {
		if (null == localName || localName.trim().isEmpty())
			return;

		NodeList nodeList = parent.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);
			if (localName.equals(child.getLocalName()))
				nodes.add(child);
			else {
				findAllByLocalNamePriv(nodes, child, localName);
			}
		}
	}

	public static List<Node> findAllByLocalNameAndNS(Node parent, String localName, String namespace) {
		List<Node> nodes = new ArrayList<Node>();
		findAllByLocalNameAndNSPriv(nodes, parent, localName, namespace);
		return nodes;
	}

	private static void findAllByLocalNameAndNSPriv(List<Node> nodes, Node parent, String localName, String namespace) {
		if (null == localName || localName.trim().isEmpty())
			return;

		NodeList nodeList = parent.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node child = nodeList.item(i);
			if (localName.equals(child.getLocalName()) && namespace.equals(child.getNamespaceURI()))
				nodes.add(child);
			else {
				findAllByLocalNameAndNSPriv(nodes, child, localName, namespace);
			}
		}
	}
}
