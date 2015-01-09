package org.devel.jerseyfx.server.rs;

import java.util.Collection;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.devel.jerseyfx.common.model.Person;
import org.devel.jerseyfx.common.rs.IPeople;

@Singleton
//@PermitAll
public class PeopleSecure extends People implements IPeople {

	private static final Logger logger = Logger.getLogger(PeopleSecure.class);

	/*
	 * Injecting SecurityContext on field layer. (Works only with singleton
	 * resources.)
	 */
	@Context
	private SecurityContext securityContext;

	/*
	 * Parameter injection of SecurityContext (request context).
	 */
	@GET
	@Path("print")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response printSecurityContext(
			// final HttpServletRequest request,
			@Context final SecurityContext securityContext) {

		logger.info("########################## Parameter injection of SecurityContext ###########################");
		logger.info("Access happens in a secure way: "
				+ securityContext.isSecure());
		logger.info("Authentication scheme: "
				+ securityContext.getAuthenticationScheme());
		logger.info("Name of currently authenticated user: "
				+ securityContext.getUserPrincipal());
		logger.info("Currently authenticated user is in role: "
				+ securityContext.isUserInRole("user"));

		return Response.ok().build();
	}

	/*
	 * Authentication example using SecurityContext.
	 */
	@Override
	public Response addPerson(String email, String firstName, String lastName) {
		if (securityContext.isSecure() && securityContext.isUserInRole("admin")) {
			return super.addPerson(email, firstName, lastName);
		}
		return Response.notModified().build();
	}

	/*
	 * Securing JAX-RS resources with standard javax.annotation.security
	 * annotations.
	 */
	@Override
	//@RolesAllowed("user")
	public Person getPerson(String email) {
		return super.getPerson(email);
	}

	@Override
	@RolesAllowed("user")
	public Collection<Person> getPeople(int page) {
		return super.getPeople(page);
	}

	@Override
	@RolesAllowed("admin")
	public Person updatePerson(String email, String firstName, String lastName) {
		return super.updatePerson(email, firstName, lastName);
	}

	@Override
	@RolesAllowed("admin")
	public Response deletePerson(String email) {
		return super.deletePerson(email);
	}

}
