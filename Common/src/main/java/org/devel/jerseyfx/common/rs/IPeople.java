package org.devel.jerseyfx.common.rs;

import java.util.Collection;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.devel.jerseyfx.common.model.Person;

@Path("people")
public interface IPeople {

	/*
	 * POST == CREATE
	 */
	@Produces({ MediaType.APPLICATION_JSON })
	@POST
	public abstract Response addPerson(
			@FormParam("email") final String email,
			@FormParam("firstName") final String firstName,
			@FormParam("lastName") final String lastName);

	/*
	 * GET == RECEIVE
	 */
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{email}")
	@GET
	public abstract Person getPerson(@PathParam("email") final String email);

	@Produces({ MediaType.APPLICATION_JSON })
	@GET
	public abstract Collection<Person> getPeople(
			@QueryParam("page") @DefaultValue("1") final int page);

	/*
	 * PUT (ID known to the client) == UPDATE
	 */
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("{email}")
	@PUT
	public abstract Person updatePerson(@PathParam("email") final String email,
			@FormParam("firstName") final String firstName,
			@FormParam("lastName") final String lastName);

	/*
	 * DELETE
	 */
	@Path("{email}")
	@DELETE
	public abstract Response deletePerson(@PathParam("email") final String email);
	
}
