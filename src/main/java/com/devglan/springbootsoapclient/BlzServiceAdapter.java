package com.devglan.springbootsoapclient;

import com.devglan.springbootsoapclient.generated.blz.GetBankResponseType;
import org.springframework.ws.FaultAwareWebServiceMessage;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.support.MarshallingUtils;

import javax.xml.bind.JAXBElement;
import java.io.IOException;

public class BlzServiceAdapter extends WebServiceGatewaySupport {

	public GetBankResponseType getBank(String url, Object requestPayload){
		WebServiceTemplate webServiceTemplate = getWebServiceTemplate();
		JAXBElement<GetBankResponseType> res = (JAXBElement<GetBankResponseType>) webServiceTemplate.sendAndReceive(url, new WebServiceMessageCallback(){

			@Override
			public void doWithMessage(final WebServiceMessage request) throws IOException {
				if (requestPayload != null) {
					if (webServiceTemplate.getMarshaller() == null) {
						throw new IllegalStateException("No marshaller registered. Check configuration of WebServiceTemplate.");
					}
					MarshallingUtils.marshal(webServiceTemplate.getMarshaller(), requestPayload, request);
				}
			}
		}, new WebServiceMessageExtractor<Object>() {

			@Override
			public Object extractData(final WebServiceMessage response) throws IOException {
				if (webServiceTemplate.getUnmarshaller() == null) {
					throw new IllegalStateException("No unmarshaller registered. Check configuration of WebServiceTemplate.");
				}
				if (hasFault(response)) {
					throw new SoapFaultClientException((SoapMessage) response);
				} else {
					return MarshallingUtils.unmarshal(webServiceTemplate.getUnmarshaller(), response);
				}
			}

			protected boolean hasFault(final WebServiceMessage response) {
				if (response instanceof FaultAwareWebServiceMessage) {
					FaultAwareWebServiceMessage faultMessage = (FaultAwareWebServiceMessage) response;
					return faultMessage.hasFault();
				}
				return false;
			}
		});
		return res.getValue();
	}
}