package org.devel.springfx.server.config;

import java.util.Arrays;

import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.devel.springfx.server.rs.IPeopleRestService;
import org.devel.springfx.server.rs.JaxRsApiApplication;
import org.devel.springfx.server.rs.PeopleRestService;
import org.devel.springfx.server.services.PeopleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class GlobalConfiguration {
	
	@Bean( destroyMethod = "shutdown" )
	public SpringBus cxf() {
		return new SpringBus();
	}
	
	@Bean @DependsOn( "cxf" )
	public Server jaxRsServer() {
		JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint( jaxRsApiApplication(), JAXRSServerFactoryBean.class );
		factory.setServiceBeans( Arrays.< Object >asList( peopleRestService() ) );
		factory.setAddress( /* "/" + */ factory.getAddress() );
		factory.setProviders( Arrays.< Object >asList( jsonProvider() ) );
		return factory.create();
	}
	
	@Bean 
	public JaxRsApiApplication jaxRsApiApplication() {
		return new JaxRsApiApplication();
	}
	
	@Bean 
	public IPeopleRestService peopleRestService() {
		return new PeopleRestService();
	}
	
	@Bean 
	public PeopleService peopleService() {
		return new PeopleService();
	}
		
	@Bean
	public JacksonJsonProvider jsonProvider() {
		return new JacksonJsonProvider();
	}
}
