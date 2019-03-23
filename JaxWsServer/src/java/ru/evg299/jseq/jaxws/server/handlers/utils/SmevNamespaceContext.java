package ru.evg299.jseq.jaxws.server.handlers.utils;

import ru.evg299.jseq.jaxws.server.classes.spec255.XML_CONSTANTS;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 * Created by Evgeny(e299792459@gmail.com) on 16.02.14.
 */
public class SmevNamespaceContext implements NamespaceContext {

    @Override
    public String getNamespaceURI(String prefix) {
        if (prefix == null)
            throw new NullPointerException("Null prefix");
        else if ("smev".equals(prefix))
            return XML_CONSTANTS.smev;
        return XMLConstants.DEFAULT_NS_PREFIX;
    }

    // Не нужен для XPath
    @Override
    public String getPrefix(String namespaceURI) {
        throw new UnsupportedOperationException();
    }

    // Не нужен для XPath
    @Override
    public Iterator<?> getPrefixes(String namespaceURI) {
        throw new UnsupportedOperationException();
    }

}
