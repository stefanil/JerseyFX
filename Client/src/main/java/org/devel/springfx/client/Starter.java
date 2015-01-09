package org.devel.springfx.client;

import javax.net.ssl.SSLContext;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.devel.jerseyfx.common.model.Person;
import org.glassfish.jersey.SslConfigurator;

public class Starter /* extends Application */{

	private static final String TEST_E_MAIL = "stefan.illgen@mail.com";
	private static final Logger logger = Logger.getLogger(Starter.class);

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		runClient();
	}

	private static void runClient() {

		WebTarget target = createTargetSecure();
//		WebTarget target = createTargetDefault();

		try {
			createPerson(target);
			readPerson(target);
			updatePerson(target);
			deletePerson(target);
		} catch (ResponseProcessingException e) {
			e.printStackTrace();
		} catch (ProcessingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	private static WebTarget createTargetDefault() {
		
		Client client = ClientBuilder.newClient();
		
		/*
		 * Creating an arbitrary stub won't work!
		 */
		// IPeopleRestService peopleRestService =
		// ClientBuilder.newClient().target("http://localhost:9000/")
		// .path("/people")
		// .queryParam("page", 1)
		// .request(MediaType.APPLICATION_JSON)
		// .get(IPeopleRestService.class);
		//
		// // (0) add person: remote POST call to
		// http://localhost:8080/rest/api/people
		// peopleRestService.addPerson("stefan.illgen.ebenheit@gmail.com",
		// "Stefan", "Illgen");
		//
		// // (1) show people: remote GET call to
		// http://localhost:8080/rest/api/people
		// Collection<Person> people = peopleRestService.getPeople(1);
		// for (Person person : people) {
		// System.out.println("Email: " + person.getEmail());
		// System.out.println("First name: " + person.getFirstName());
		// System.out.println("Last name: " + person.getLastName());
		// }

		// With jersey its all about a WebTarget ..
		WebTarget target = client
				.target("http://localhost:9000/").path("/people");
		
		return target;
	}

	private static WebTarget createTargetSecure() {
		SslConfigurator sslConfig = SslConfigurator.newInstance();
//		        .trustStoreFile("./truststore_client")
//		        .trustStorePassword("secret-password-for-truststore")
//		        .keyStoreFile("./keystore_client")
//		        .keyPassword("secret-password-for-keystore");
		SSLContext sslContext = sslConfig.createSSLContext();
		Client client = ClientBuilder.newBuilder().sslContext(sslContext).build();
		WebTarget target = client
				.target("https://localhost/").path("/people");
		return target;
	}

	private static void createPerson(WebTarget target) throws Exception {

		logger.info("########################### CREATE ############################");

		// try {

		Response response = target.request(MediaType.APPLICATION_JSON).post(
				Entity.entity(
						new Form().param("email", TEST_E_MAIL)
								.param("firstName", "Stefan")
								.param("lastName", "Illgen"),
						MediaType.APPLICATION_FORM_URLENCODED_TYPE));

		/*
		 * This won't work! There is always a response returned also in case of
		 * an WebApplicationException. WebApplicationException get automatically
		 * converted into a response.
		 */
		// } catch (PersonAlreadyExistsException e) {
		// e.printStackTrace();
		// } catch (WebApplicationException e) {
		// e.printStackTrace();
		// }

		logger.debug(response);

		// better switch on response status family
		// type
		switch (response.getStatusInfo().getFamily()) {
		case SUCCESSFUL:
		case SERVER_ERROR:
		case CLIENT_ERROR:
		case INFORMATIONAL:
		case REDIRECTION:
		case OTHER:
		default:
			logger.info("Response Family: "
					+ response.getStatusInfo().getFamily());
			// show to client
			break;
		}

		// .. or directly on status
		if (response.getStatus() == Status.OK.getStatusCode()) {
			// do sth.
		} else if (response.getStatus() == Status.ACCEPTED.getStatusCode()) {
			// do sth.
		} else if (response.getStatus() == Status.CONFLICT.getStatusCode()) {
			// do sth.
		} else if (response.getStatus() == Status.UNAUTHORIZED.getStatusCode()) {
			// do sth.
		} else if (response.getStatus() == Status.BAD_GATEWAY.getStatusCode()) {
			// do sth.
		} else if (response.getStatus() == Status.BAD_REQUEST.getStatusCode()) {
			// do sth.
		} else if (response.getStatus() == Status.NOT_ACCEPTABLE
				.getStatusCode()) {
			// do sth.
		} else if (response.getStatus() == Status.PAYMENT_REQUIRED
				.getStatusCode()) {
			// do sth.
		} else if (response.getStatus() == Status.MOVED_PERMANENTLY
				.getStatusCode()) {
			// do sth.
		} else {
			// do sth.
		}

		// show status and the response message to the client
		logger.info("Status Code: " + response.getStatusInfo().getStatusCode());
		logger.info("Reason Phrase: "
				+ response.getStatusInfo().getReasonPhrase());

	}

	private static void readPerson(WebTarget target) throws Exception {

		logger.info("########################### READ ############################");

		/*
		 * A non supported type will cause a ProcessingException!
		 */
		// logger.info(target.queryParam("page", 1)
		// .request(MediaType.APPLICATION_JSON)
		// .get(Collection.class));

		/*
		 * Built-in support for Java types of representations such as byte[],
		 * String, Number, Boolean, Character, InputStream, java.io.Reader,
		 * File, DataSource, additional Jersey-specific JSON and Multi Part
		 * support ..
		 */
		logger.info("GET request with collection serialized 2 a String: "
				+ target.queryParam("page", 1)
						.request(MediaType.APPLICATION_JSON).get(String.class));

		/*
		 * .. as well as JAXB beans ..
		 */
		Person person = target.path(TEST_E_MAIL)
				.request(MediaType.APPLICATION_JSON).get(Person.class);
		logger.info("Typed GET request using the java beans conform model type Person: \n\n"
				+ person + "\n");

		/*
		 * .. even if they contain star associations.
		 */
		logger.info("1st parent: " + person.getParents().get(0) + "\n");

	}

	private static void updatePerson(WebTarget target) throws Exception {

		logger.info("########################### UPDATE ############################");

		Person person = target
				.path(TEST_E_MAIL)
				.request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(new Form().param("firstName", "Tobias")
						.param("lastName", "Schmidt"),
						MediaType.APPLICATION_FORM_URLENCODED_TYPE),
						Person.class);

		logger.info("Updated person: \n\n" + person + "\n");

	}

	private static void deletePerson(WebTarget target) throws Exception {

		logger.info("########################### DELETE ############################");

		Response response = target.path(TEST_E_MAIL)
				.request(MediaType.APPLICATION_JSON).delete();

		logger.info("Deletion of person identified by \"" + TEST_E_MAIL
				+ "\": " + response.getStatusInfo().getFamily());

	}

}
