package com.devglan.springbootsoapclient;

import com.devglan.springbootsoapclient.generated.blz.GetBankResponseType;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;

import javax.xml.bind.JAXBElement;

public class BlzServiceAdapter extends WebServiceGatewaySupport {

	public GetBankResponseType getBank(String url, Object requestPayload){
		WebServiceTemplate webServiceTemplate = getWebServiceTemplate();
		JAXBElement<GetBankResponseType> res = null;
		try {
			res = (JAXBElement<GetBankResponseType>) webServiceTemplate.marshalSendAndReceive(url, requestPayload);
		}catch(SoapFaultClientException ex){
			System.out.println(ex.getMessage());
		}
		return res.getValue();
	}
}