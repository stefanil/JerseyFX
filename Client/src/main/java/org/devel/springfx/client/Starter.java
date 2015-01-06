package org.devel.springfx.client;

import javax.ws.rs.client.ClientBuilder;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Starter /* extends Application */{

	protected static final Logger logger = Logger.getLogger(Starter.class);

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		runClient();
	}

	private static void runClient() {
		logger.info(ClientBuilder.newClient().target("http://localhost:9000/")
				.path("myresource")
				.request().get(String.class));
	}

}
