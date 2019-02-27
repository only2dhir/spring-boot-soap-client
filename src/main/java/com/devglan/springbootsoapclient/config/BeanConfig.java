package com.devglan.springbootsoapclient.config;

import com.devglan.springbootsoapclient.BlzServiceAdapter;
import com.devglan.springbootsoapclient.SoapClientInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

@Configuration
public class BeanConfig {

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.devglan.springbootsoapclient.generated.blz");
		return marshaller;
	}

	@Bean
	public BlzServiceAdapter soapConnector(Jaxb2Marshaller marshaller) {
		BlzServiceAdapter client = new BlzServiceAdapter();
		client.setDefaultUri("http://www.thomas-bayer.com/axis2/services/BLZService");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		ClientInterceptor[] interceptors = new ClientInterceptor[] {interceptor()};
		client.setInterceptors(interceptors);
		return client;
	}

	@Bean
	public SoapClientInterceptor interceptor() {
		return new SoapClientInterceptor();
	}

}
