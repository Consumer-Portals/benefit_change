package com.bcbst.benefitchange.util;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SOAPLogHandler implements SOAPHandler<SOAPMessageContext> {
	private Logger logger = LogManager.getLogger(SOAPLogHandler.class);

	@Override
	public Set<QName> getHeaders() {
		// Auto-generated method stub
		return new HashSet<>();
	}

	@Override
	public void close(MessageContext arg0) {
		// Auto-generated method stub
		
	}

	@Override
	public boolean handleFault(SOAPMessageContext arg0) {
		SOAPMessage message= arg0.getMessage();
		try {
			message.writeTo(System.out);
		} catch (SOAPException | IOException e) {
			logger.error(e);
		}
		return false;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext arg0) {
		SOAPMessage message= arg0.getMessage();
		boolean isOutboundMessage=  (Boolean)arg0.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if(isOutboundMessage){
			logger.debug("OUTBOUND MESSAGE\n");
			
		}else{
			logger.debug("INBOUND MESSAGE\n");
		}
		try {
			message.writeTo(System.out);
		} catch (SOAPException | IOException e) {
			logger.error(e);
		}
		return false;
	}

}
