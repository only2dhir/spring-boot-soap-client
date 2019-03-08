package com.devglan.springbootsoapclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class SoapClientInterceptor implements ClientInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SoapClientInterceptor.class);

    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        logger.info("intercepted a fault.");
        WebServiceMessage message = messageContext.getResponse();
        SaajSoapMessage saajSoapMessage = (SaajSoapMessage)message;
        SOAPMessage soapMessage = saajSoapMessage.getSaajMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        try {
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPBody soapBody = soapEnvelope.getBody();
            SOAPFault soapFault = soapBody.getFault();
            System.out.println(soapFault.getFaultString());
            StringWriter sw = new StringWriter();
            TransformerFactory.newInstance().newTransformer().transform(
                    new DOMSource(soapFault), new StreamResult(sw));
            String xml = sw.toString();
            System.out.println(xml);
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {
    }
}
