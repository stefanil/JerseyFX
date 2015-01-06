package org.devel.springfx.server.rs;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.devel.springfx.server.model.Person;
import org.devel.springfx.server.services.PeopleService;

public class PeopleRestService implements IPeopleRestService {

	@Inject
	private PeopleService peopleService;

	@Override
	public Collection<Person> getPeople(final int page) {
		return peopleService.getPeople(page, 5);
	}

	@Override
	public Person getPeople(final String email) {
		return peopleService.getByEmail(email);
	}

	@Override
	public Response addPerson(// final UriInfo uriInfo, 
			final String email,
			final String firstName, final String lastName) {
		peopleService.addPerson(email, firstName, lastName);
		return Response.ok().build();
//		return		Response.created(
//				uriInfo.getRequestUriBuilder().path(email).build()).build();
	}

	@Override
	public Person updatePerson(final String email, final String firstName,
			final String lastName) {
		final Person person = peopleService.getByEmail(email);
		if (firstName != null) {
			person.setFirstName(firstName);
		}
		if (lastName != null) {
			person.setLastName(lastName);
		}
		return person;
	}

	@Override
	public Response deletePerson(final String email) {
		peopleService.removePerson(email);
		return Response.ok().build();
	}

}
