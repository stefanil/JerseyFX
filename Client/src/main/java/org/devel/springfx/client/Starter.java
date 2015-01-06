package org.devel.springfx.client;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.devel.springfx.server.model.Person;
import org.devel.springfx.server.rs.IPeopleRestService;

//import javafx.application.Application;
//import javafx.stage.Stage;

public class Starter /* extends Application */{

	public static void main(String[] args) {
		accessViaCXFProxy();
		// launch(args);
	}

	private static void accessViaCXFProxy() {
		IPeopleRestService peopleRestService = JAXRSClientFactory.create(
				"http://localhost:8080/rest/api", IPeopleRestService.class,
				new ArrayList<Object>() {
					private static final long serialVersionUID = -6495579098861723182L;
					{
						add(new JacksonJaxbJsonProvider());
					}
				});
		// (0) add person: remote POST call to http://localhost:8080/rest/api/people
		peopleRestService.addPerson("stefan.illgen.ebenheit@gmail.com",
				"Stefan", "Illgen");
		// (1) show people: remote GET call to http://localhost:8080/rest/api/people
		Collection<Person> people = peopleRestService.getPeople(1);
		for (Person person : people) {
			System.out.println("Email: " + person.getEmail());
			System.out.println("First name: " + person.getFirstName());
			System.out.println("Last name: " + person.getLastName());
		}
	}

	// public void start(Stage stage) throws Exception {
	// // load view tuple
	// ViewLoader viewLoader = new ViewLoader();
	// ViewTuple<SearchRouteViewModel> viewTuple =
	// viewLoader.loadViewTuple(SearchRouteView.class);
	// // configure the stage
	// stage.setScene(new Scene(viewTuple.getView()));
	// stage.setTitle(Configuration.APPLICATION_TITLE);
	// // stage.setHeight(Configuration.APPLICATION_SCREEN_HEIGHT);
	// // stage.setWidth(Configuration.APPLICATION_SCREEN_WIDTH);
	// // show the stage
	// stage.show();
	// }

}
