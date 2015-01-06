package org.devel.springfx.client;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Starter /* extends Application */{

	private static final Logger logger = Logger.getLogger(Starter.class);

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		runClient();
	}

	private static void runClient() {

		// logger.info(ClientBuilder.newClient().target("http://localhost:9000/")
		// .path("myresource").request().get(String.class));

		WebTarget target = ClientBuilder.newClient()
				.target("http://localhost:9000/").path("/people");

		Form form = new Form();
		form.param("x", "foo");
		form.param("y", "bar");

		logger.info(target.request(MediaType.APPLICATION_JSON).post(
				Entity.entity(
						new Form()
								.param("email",
										"stefan.illgen.ebenheit@gmail.com")
								.param("firstName", "Stefan")
								.param("lastName", "Illgen"),
						MediaType.APPLICATION_FORM_URLENCODED_TYPE)));
		logger.info(target.queryParam("page", 1)
				.request(MediaType.APPLICATION_JSON).get(String.class));

		// Won't work!
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
	}
}
