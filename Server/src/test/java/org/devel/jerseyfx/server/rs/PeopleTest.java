package org.devel.jerseyfx.server.rs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;

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
import javax.ws.rs.core.UriBuilder;

import org.devel.jerseyfx.common.model.Person;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;

public class PeopleTest extends GrizzlyTest {

	private static final String TEST_E_MAIL = "stefan.illgen@mail.com";
	
	@Override
	protected HttpServer createServer() {
		URI baseUri = UriBuilder.fromUri("http://localhost/").port(9000)
				.build();
		ResourceConfig config = new ResourceConfig(People.class);
		return GrizzlyHttpServerFactory.createHttpServer(baseUri, config, false);
	}

	protected WebTarget createWebTarget() {
		Client client = ClientBuilder.newClient();

		/*
		 * Creating an arbitrary stub won't work with Jersey!
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
		return client.target("http://localhost:9000/").path("/people");
	}

	@Test
	public void testPerson() {
		createPerson();
		readPeople();
		readPerson();
		updatePerson();
		deletePerson();
	}

	public void createPerson() {
		try {
			// try {
			Response response = target.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(
							new Form().param("email", TEST_E_MAIL)
									.param("firstName", "Stefan")
									.param("lastName", "Illgen"),
							MediaType.APPLICATION_FORM_URLENCODED_TYPE));

			/*
			 * This won't work! There is always a response returned also in case
			 * of an WebApplicationException. WebApplicationException get
			 * automatically converted into a response.
			 */
			// } catch (PersonAlreadyExistsException e) {
			// e.printStackTrace();
			// } catch (WebApplicationException e) {
			// e.printStackTrace();
			// }

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
				break;
			}

			// .. or directly on status
			if (response.getStatus() == Status.OK.getStatusCode()) {
				// do sth.
			} else if (response.getStatus() == Status.ACCEPTED.getStatusCode()) {
				// do sth.
			} else if (response.getStatus() == Status.CONFLICT.getStatusCode()) {
				// do sth.
			} else if (response.getStatus() == Status.UNAUTHORIZED
					.getStatusCode()) {
				// do sth.
			} else if (response.getStatus() == Status.BAD_GATEWAY
					.getStatusCode()) {
				// do sth.
			} else if (response.getStatus() == Status.BAD_REQUEST
					.getStatusCode()) {
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

			assertEquals(Status.Family.SUCCESSFUL, response.getStatusInfo()
					.getFamily());
			assertEquals(Status.OK.getStatusCode(), response.getStatusInfo()
					.getStatusCode());
			assertTrue(response.getStatusInfo().getReasonPhrase() instanceof String);

		} catch (ResponseProcessingException e) {
			e.printStackTrace();
			assertTrue(e.getMessage(), false);
		} catch (ProcessingException e) {
			e.printStackTrace();
			assertTrue(e.getMessage(), false);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(e.getMessage(), false);
		}
	}

	public void readPeople() {
		try {
			/*
			 * A non supported type will cause a ProcessingException!
			 */
			// logger.info(target.queryParam("page", 1)
			// .request(MediaType.APPLICATION_JSON)
			// .get(Collection.class));

			/*
			 * Built-in support for Java types of representations such as
			 * byte[], String, Number, Boolean, Character, InputStream,
			 * java.io.Reader, File, DataSource, additional Jersey-specific JSON
			 * and Multi Part support ..
			 */
			String sResponse = target.queryParam("page", 1)
					.request(MediaType.APPLICATION_JSON).get(String.class);

			assertTrue(sResponse instanceof String);

		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(e.getMessage(), false);
		}
	}

	public void readPerson() {
		try {
			/*
			 * Built-in support for Java beans ..
			 */
			Person person = target.path(TEST_E_MAIL)
					.request(MediaType.APPLICATION_JSON).get(Person.class);
			assertTrue(person.getEmail().equals(TEST_E_MAIL));
			/*
			 * .. even if they contain star associations.
			 */
			assertEquals(1, person.getParents().size());
			assertTrue(person.getParents().get(0) instanceof Person);

		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(e.getMessage(), false);
		}
	}

	public void updatePerson() {
		try {

			String newFirstName = "Tobias";
			String newLastName = "Schmidt";

			Person person = target
					.path(TEST_E_MAIL)
					.request(MediaType.APPLICATION_JSON)
					.put(Entity.entity(
							new Form().param("firstName", newFirstName).param(
									"lastName", newLastName),
							MediaType.APPLICATION_FORM_URLENCODED_TYPE),
							Person.class);
			assertEquals(newFirstName, person.getFirstName());
			assertEquals(newLastName, person.getLastName());

			person = target.path(TEST_E_MAIL)
					.request(MediaType.APPLICATION_JSON).get(Person.class);
			assertEquals(newFirstName, person.getFirstName());
			assertEquals(newLastName, person.getLastName());

		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(e.getMessage(), false);
		}
	}

	public void deletePerson() {
		try {

			Response response = target.path(TEST_E_MAIL)
					.request(MediaType.APPLICATION_JSON).delete();

			assertEquals(Status.OK.getStatusCode(), response.getStatus());

		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(e.getMessage(), false);
		}
	}

}
