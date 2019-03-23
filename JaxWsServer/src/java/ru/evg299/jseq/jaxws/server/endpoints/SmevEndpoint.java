package ru.evg299.jseq.jaxws.server.endpoints;

import ru.evg299.jseq.jaxws.server.classes.spec255.SmevWraper;
import ru.evg299.jseq.jaxws.server.classes.spec255.XML_CONSTANTS;
import ru.evg299.jseq.jaxws.server.wrappers.PowMethod;

import javax.jws.*;
import javax.jws.soap.SOAPBinding;

/**
 * Created by Evgeny(e299792459@gmail.com) on 16.02.14.
 */
@WebService(targetNamespace = XML_CONSTANTS.smev)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, parameterStyle = SOAPBinding.ParameterStyle.BARE)
@HandlerChain(file = "handler-chain-smev.xml")
public class SmevEndpoint {

    @WebMethod
    @WebResult(name = XML_CONSTANTS.webMethodName)
    public SmevWraper zagsService(
            @WebParam(name = XML_CONSTANTS.webMethodName) SmevWraper smevWraper){

        PowMethod powMethod = smevWraper.getMessageData().getAppData().getPowMethod();
        powMethod.setResult(Math.pow(powMethod.getBase(), powMethod.getExponent()));

        return  smevWraper;
    }
}
