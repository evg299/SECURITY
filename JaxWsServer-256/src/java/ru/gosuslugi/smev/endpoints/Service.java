package ru.gosuslugi.smev.endpoints;

import ru.gosuslugi.smev.handlers.utils.SOAPUtil;
import ru.gosuslugi.smev.methods.SayHello;
import ru.gosuslugi.smev.rev120315.SmevHeaderType;
import ru.gosuslugi.smev.rev120315.SmevWraperType;
import ru.gosuslugi.smev.rev120315.XML_CONSTANTS;
import ru.gosuslugi.smev.utils.IdUtil;
import ru.evg299.example.appconfig.ConfigReader;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;


@WebService(targetNamespace = XML_CONSTANTS.smev)
@HandlerChain(file = "handler-chain-smev.xml")
public class Service {

    @WebMethod(operationName = XML_CONSTANTS.webMethodName)
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, parameterStyle = SOAPBinding.ParameterStyle.BARE)
    public void testSmev256(Holder<SmevHeaderType> smevHeader,
                            Holder<SmevWraperType> smevWraper) {
        /* Проверяем NodeId на кодовое слово */
        ConfigReader configReader = ConfigReader.getInstance();
        if (configReader.getPropertyValue("nvs_word").equals(smevHeader.value.getNodeId()))
            throw SOAPUtil.createSOAPFaultException("00001", "Подпись сообщения не валидна");

		/* Работа со smev:Header и smev:Message */
        IdUtil.updateReqIdRef(smevHeader, smevWraper);

		/* Обнуляем smev:Header */
        smevHeader.value = null;

		/* Обработка бизнес-логиги метода */
        SayHello sayHello = smevWraper.value.getMessageData().getAppData().getSayHello();
        sayHello.setResult("Hello " + sayHello.getName());
    }

}
