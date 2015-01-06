package org.devel.jerseyfx.server;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.devel.jerseyfx.server.rs.PeopleRestService;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Starter {

	private static final Logger logger = Logger.getLogger(Starter.class);

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		runGrizzly();
	}

	private static void runGrizzly() {
		URI baseUri = UriBuilder.fromUri("http://localhost/").port(9000)
				.build();
		ResourceConfig config = new ResourceConfig(MyResource.class, PeopleRestService.class);
		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config, false);

		// register shutdown hook
	    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        @Override
	        public void run() {
	            logger.info("Stopping server..");
	            server.shutdownNow();
	        }
	    }, "shutdownHook"));

	    // run
	    try {
	        server.start();
	        logger.info("Press CTRL^C to exit..");
	        Thread.currentThread().join();
	    } catch (Exception e) {
	        logger.error(
	                "There was an error while starting Grizzly HTTP server.", e);
	    }
	}

}
