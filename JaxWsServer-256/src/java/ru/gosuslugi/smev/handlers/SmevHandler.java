package ru.gosuslugi.smev.handlers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import ru.gosuslugi.smev.handlers.utils.SOAPDeffender;
import ru.evg299.example.appconfig.ConfigReader;
import ru.evg299.example.xmlsearch.XmlConverter;
import ru.evg299.example.xmlsearch.XmlFinder;
import ru.evg299.example.xmlsign.XMLSigner;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Set;

public class SmevHandler implements SOAPHandler<SOAPMessageContext> {

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean isOUT = (Boolean) context
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        SOAPMessage mf = context.getMessage();
        ConfigReader configReader = ConfigReader.getInstance();
        if (isOUT) {
            try {
                /* Удаляем smev:Header */
                Document document = mf.getSOAPPart().getEnvelope()
                        .getOwnerDocument();
                Node smevHeaderNode = XmlFinder.findFirstByLocalNameAndNS(
                        document, "Header",
                        "http://smev.gosuslugi.ru/rev120315");
                smevHeaderNode.getParentNode().removeChild(smevHeaderNode);

				/* Подписываем AppData */
                Node appDataNode = XmlFinder.findFirstByLocalNameAndNS(
                        document, "AppData",
                        "http://smev.gosuslugi.ru/rev120315");
                Document appDataDocument = XmlConverter
                        .createDocumentFromNode(appDataNode);
                Element signatureElement = XMLSigner.signXML(appDataDocument,
                        configReader.getPropertyValue("conainer_name"),
                        configReader.getPropertyValue("conainer_pin"));
                Node importedSignatureElement = document.importNode(
                        signatureElement, true);
                appDataNode.appendChild(importedSignatureElement);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
				/* Подписываем ответ */
                SOAPDeffender.signMessage(mf,
                        configReader.getPropertyValue("conainer_name"),
                        configReader.getPropertyValue("conainer_pin"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
			/* Обрабатываем запрос */
            try {
                /**
                 * Если нет smev:Header ставим пустой, чтоб не было ошибки от
                 * JAXB
                 */
                Document document = mf.getSOAPPart().getEnvelope()
                        .getOwnerDocument();
                Node smevHeaderNode = XmlFinder.findFirstByLocalNameAndNS(
                        document, "Header",
                        "http://smev.gosuslugi.ru/rev120315");
                if (null == smevHeaderNode) {
                    smevHeaderNode = mf.getSOAPHeader().addChildElement(
                            "Header", "smev",
                            "http://smev.gosuslugi.ru/rev120315");
                }

				/* Проверка подписи */
                if (Boolean.parseBoolean(configReader
                        .getPropertyValue("check_in_sig"))) {
                    if (!SOAPDeffender.checkSOAP(mf, true)) {
                        // Подпись не верна ставим метку для выбрасывания
                        // исключения (ставим в NodeId кодовое слово)
                        Node smevNodeIdNode = XmlFinder
                                .findFirstByLocalNameAndNS(smevHeaderNode,
                                        "NodeId",
                                        "http://smev.gosuslugi.ru/rev120315");
                        if (null == smevNodeIdNode) {
                            smevNodeIdNode = smevHeaderNode
                                    .appendChild(document
                                            .createElementNS(
                                                    "http://smev.gosuslugi.ru/rev120315",
                                                    "NodeId"));
                        }
                        smevNodeIdNode.setTextContent(configReader.getPropertyValue("nvs_word"));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        Boolean isResponse = (Boolean) context
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        SOAPMessage mf = context.getMessage();
        if (isResponse) {
			/* Подписываем ответ */
            ConfigReader configReader = ConfigReader.getInstance();
            try {
                SOAPDeffender.signMessage(mf,
                        configReader.getPropertyValue("conainer_name"),
                        configReader.getPropertyValue("conainer_pin"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void close(MessageContext context) {

    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

}
