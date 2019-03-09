package com.devglan.springbootsoapclient;

import com.devglan.springbootsoapclient.generated.blz.GetBankResponseType;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.xml.transform.StringSource;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import java.util.HashMap;
import java.util.Map;

public class BlzServiceAdapter extends WebServiceGatewaySupport {

	private static final Logger logger = LoggerFactory.getLogger(BlzServiceAdapter.class);

	@Autowired
    private Environment environment;

	public GetBankResponseType getBank(String url, Object requestPayload){
		WebServiceTemplate webServiceTemplate = getWebServiceTemplate();
		JAXBElement<GetBankResponseType> res = null;
		try {
			res = (JAXBElement<GetBankResponseType>) webServiceTemplate.marshalSendAndReceive(url, requestPayload, new WebServiceMessageCallback() {

				@Override
				public void doWithMessage(WebServiceMessage message) {
					try {
						SoapHeader soapHeader = ((SoapMessage) message).getSoapHeader();
						Map<String, Object> mapRequest = new HashMap<String, Object>();

						mapRequest.put("loginuser", environment.getProperty("soap.auth.username"));
						mapRequest.put("loginpass", environment.getProperty("soap.auth.password"));
						StringSubstitutor substitutor = new StringSubstitutor(mapRequest, "%(", ")");
						String finalXMLRequest = substitutor.replace(environment.getProperty("soap.auth.header"));
						StringSource headerSource = new StringSource(finalXMLRequest);
						Transformer transformer = TransformerFactory.newInstance().newTransformer();
						transformer.transform(headerSource, soapHeader.getResult());
						logger.info("Marshalling of SOAP header success.");
					} catch (Exception e) {
						logger.error("error during marshalling of the SOAP headers", e);
					}
				}
			});
		}catch (SoapFaultClientException e){
			logger.error("Error while invoking session service : " + e.getMessage());
			logger.error("Fault code : " + e.getFaultCode().toString());
			if(e.getSoapFault().getFaultCode() != null) {
				logger.error("Fault code : " + e.getSoapFault().getFaultCode().getPrefix());
			}
			return null;
		}
		return res.getValue();
	}
}