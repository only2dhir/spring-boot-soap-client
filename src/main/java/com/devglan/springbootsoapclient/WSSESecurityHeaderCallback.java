package com.devglan.springbootsoapclient;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import javax.xml.soap.*;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class WSSESecurityHeaderCallback implements WebServiceMessageCallback {

    public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {

        try {

            SaajSoapMessage saajSoapMessage = (SaajSoapMessage)message;

            SOAPMessage soapMessage = saajSoapMessage.getSaajMessage();

            SOAPPart soapPart = soapMessage.getSOAPPart();

            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();

            SOAPHeader soapHeader = soapEnvelope.getHeader();

            Name headerElementName = soapEnvelope.createName(
                            "Security",
                            "wsse",
                            "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
                    );

            // Add "Security" soapHeaderElement to soapHeader
            SOAPHeaderElement soapHeaderElement = soapHeader.addHeaderElement(headerElementName);

            Name headerElementName1 = soapEnvelope.createName(
                    "dddd",
                    "fgfgf",
                    "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
            );
            SOAPHeaderElement soapHeaderElement1 = soapHeader.addHeaderElement(headerElementName1);

            // Add usernameToken to "Security" soapHeaderElement
            SOAPElement usernameTokenSOAPElement = soapHeaderElement.addChildElement("UsernameToken");

            // Add username to usernameToken
            SOAPElement userNameSOAPElement = usernameTokenSOAPElement.addChildElement("Username");

            userNameSOAPElement.addTextNode("myUserName");

            // Add password to usernameToken
            SOAPElement passwordSOAPElement = usernameTokenSOAPElement.addChildElement("Password");

            passwordSOAPElement.addTextNode("myPassword");
            soapMessage.saveChanges();
        } catch (SOAPException soapException) {
            throw new RuntimeException("WSSESecurityHeaderCallback", soapException);
        }
    }
}