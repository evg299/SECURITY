package ru.gosuslugi.smev.utils;

import javax.xml.ws.Holder;

import ru.gosuslugi.smev.rev120315.SmevHeaderType;
import ru.gosuslugi.smev.rev120315.SmevWraperType;

public class IdUtil {

	public static void updateReqIdRef(Holder<SmevHeaderType> smevHeader,
			Holder<SmevWraperType> smevWraper) {
		if (null != smevHeader && null != smevWraper) {
			String originRequestIdRef = smevWraper.value.getMessage()
					.getOriginRequestIdRef();

			if (null != smevHeader.value.getMessageId())
				smevWraper.value.getMessage().setRequestIdRef(
						smevHeader.value.getMessageId());

			if (null == originRequestIdRef) {
				smevWraper.value.getMessage().setOriginRequestIdRef(
						smevHeader.value.getMessageId());
			}
		}
	}
}
