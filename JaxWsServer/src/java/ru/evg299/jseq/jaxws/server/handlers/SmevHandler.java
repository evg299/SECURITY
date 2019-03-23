package ru.evg299.jseq.jaxws.server.handlers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import ru.evg299.jseq.jaxws.server.signature.ConfigSignUtil;
import ru.evg299.jseq.jaxws.server.classes.spec255.XML_CONSTANTS;
import ru.evg299.jseq.jaxws.server.handlers.utils.SignatureUtil;
import ru.evg299.jseq.jaxws.server.handlers.utils.SmevNamespaceContext;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.xpath.*;
import java.util.Set;

/**
 * Created by Evgeny(e299792459@gmail.com) on 16.02.14.
 */
public class SmevHandler implements SOAPHandler<SOAPMessageContext> {
    /**
     * Gets the header blocks that can be processed by this Handler
     * instance.
     *
     * @return Set of <code>QNames</code> of header blocks processed by this
     * handler instance. <code>QName</code> is the qualified
     * name of the outermost element of the Header block.
     */
    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    /**
     * The <code>handleMessage</code> method is invoked for normal processing
     * of inbound and outbound messages. Refer to the description of the handler
     * framework in the JAX-WS specification for full details.
     *
     * @param context the message context.
     * @return An indication of whether handler processing should continue for
     * the current message
     * <ul>
     * <li>Return <code>true</code> to continue
     * processing.</li>
     * <li>Return <code>false</code> to block
     * processing.</li>
     * </ul>
     * @throws RuntimeException               Causes the JAX-WS runtime to cease
     *                                        handler processing and generate a fault.
     * @throws javax.xml.ws.ProtocolException Causes the JAX-WS runtime to switch to
     *                                        fault message processing.
     */
    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean isResponse = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        ConfigSignUtil.ConfigSign configSign = ConfigSignUtil.getConfigSign();

        SOAPMessage mf = context.getMessage();
        if (isResponse) {
            /* Подписываем ответ */
            if (null != configSign) {
                try {
                    SignatureUtil.signSOAP(mf, configSign.getCertId(),
                            configSign.getContName(), configSign.getContPass());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            /* Обрабатываем запрос */
            try {
                if (configSign.isCheckInputSignature()) {
                    /* Проверка подписи */
                    if (!SignatureUtil.checkSOAP(mf, true)) {
                        System.out.println("Подпись конверта не верна");
                        return false;
                    }
                }

				/* Работа со smev:header */
                SOAPEnvelope envelope = mf.getSOAPPart().getEnvelope();
                Document soapAsDocument = envelope.getOwnerDocument();
                // System.out.println(soapAsDocument);
                XPathFactory factory = XPathFactory.newInstance();

                // 1. найти значение //smev:MessageId
                Node messageIdNode = this.getNodeByXPath("//smev:MessageId",
                        factory, soapAsDocument);
                // System.out.println("messageIdNode: " + messageIdNode);
                if (null != messageIdNode) {
                    String messageIdString = messageIdNode.getTextContent();
                    messageIdString = this.convertTextContent(messageIdString);

                    // 1.5 найти узел //smev:Message
                    Node messageNode = this.getNodeByXPath("//smev:Message",
                            factory, soapAsDocument);

                    // 2. найти значение //smev:RequestIdRef
                    String requestIdRefString = "";
                    Node requestIdRefNode = this.getNodeByXPath(
                            "//smev:RequestIdRef", factory, soapAsDocument);

                    // System.out.println("requestIdRefNode: " +
                    // requestIdRefNode);
                    if (null != requestIdRefNode) {
                        requestIdRefString = requestIdRefNode.getTextContent();
                        requestIdRefString = this
                                .convertTextContent(requestIdRefString);
                    }

                    // 3. найти значение //smev:OriginRequestIdRef
                    String originRequestIdRefString = "";
                    Node originRequestIdRefNode = this.getNodeByXPath(
                            "//smev:OriginRequestIdRef", factory,
                            soapAsDocument);
                    // System.out.println("originRequestIdRefNode: " +
                    // originRequestIdRefNode);
                    if (null != originRequestIdRefNode) {
                        originRequestIdRefString = originRequestIdRefNode
                                .getTextContent();
                        originRequestIdRefString = this
                                .convertTextContent(originRequestIdRefString);
                    }

                    if ("".equals(requestIdRefString)
                            && "".equals(originRequestIdRefString)) {
                        // Запрос синхронный или начало асинхронного
                        if (null != requestIdRefNode)
                            requestIdRefNode.setTextContent(messageIdString);
                        else {
                            Element requestIdRef = soapAsDocument
                                    .createElementNS(XML_CONSTANTS.smev,
                                            "RequestIdRef");
                            requestIdRef.setTextContent(messageIdString);
                            requestIdRefNode = messageNode
                                    .appendChild(requestIdRef);
                        }

                        if (null != originRequestIdRefNode)
                            originRequestIdRefNode
                                    .setTextContent(messageIdString);
                        else {
                            Element originRequestIdRef = soapAsDocument
                                    .createElementNS(XML_CONSTANTS.smev,
                                            "OriginRequestIdRef");
                            originRequestIdRef.setTextContent(messageIdString);
                            requestIdRefNode = messageNode
                                    .appendChild(originRequestIdRef);
                        }
                    } else {
                        // Продолжение асинхронного запроса
                        if (null != requestIdRefNode)
                            requestIdRefNode.setTextContent(messageIdString);
                        else {
                            Element requestIdRef = soapAsDocument
                                    .createElementNS(XML_CONSTANTS.smev,
                                            "RequestIdRef");
                            requestIdRef.setTextContent(messageIdString);
                            requestIdRefNode = messageNode
                                    .appendChild(requestIdRef);
                        }
                    }
                }
            } catch (SOAPException e) {
                e.printStackTrace();
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    /**
     * The <code>handleFault</code> method is invoked for fault message
     * processing.  Refer to the description of the handler
     * framework in the JAX-WS specification for full details.
     *
     * @param context the message context
     * @return An indication of whether handler fault processing should continue
     * for the current message
     * <ul>
     * <li>Return <code>true</code> to continue
     * processing.</li>
     * <li>Return <code>false</code> to block
     * processing.</li>
     * </ul>
     * @throws RuntimeException               Causes the JAX-WS runtime to cease
     *                                        handler fault processing and dispatch the fault.
     * @throws javax.xml.ws.ProtocolException Causes the JAX-WS runtime to cease
     *                                        handler fault processing and dispatch the fault.
     */
    @Override
    public boolean handleFault(SOAPMessageContext context) {
        Boolean isResponse = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        ConfigSignUtil.ConfigSign configSign = ConfigSignUtil.getConfigSign();
        SOAPMessage mf = context.getMessage();
        if (isResponse) {
			/* Подписываем ответ */
            if (null != configSign) {
                try {
                    SignatureUtil.signSOAP(mf, configSign.getCertId(),
                            configSign.getContName(), configSign.getContPass());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    /**
     * Called at the conclusion of a message exchange pattern just prior to
     * the JAX-WS runtime disptaching a message, fault or exception.  Refer to
     * the description of the handler
     * framework in the JAX-WS specification for full details.
     *
     * @param context the message context
     */
    @Override
    public void close(MessageContext context) {

    }

    private Node getNodeByXPath(String query, XPathFactory factory,
                                Document document) throws XPathExpressionException {
        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new SmevNamespaceContext());
        XPathExpression expr = xpath.compile(query);
        return (Node) expr.evaluate(document, XPathConstants.NODE);
    }

    private String convertTextContent(String content) {
        if (null == content)
            return "";
        else
            return content.trim();
    }
}
