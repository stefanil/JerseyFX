package org.devel.jerseyfx.server.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthorizationRequestFilter implements ContainerRequestFilter {

	private static final String GROUP_ADMIN = "admin";
	private static final String GROUP_USER = "user";

	@Override
	public void filter(final ContainerRequestContext requestContext)
			throws IOException {

		final SecurityContext wrappedSecurityContext = requestContext
				.getSecurityContext();

		requestContext.setSecurityContext(new SecurityContext() {

			@Override
			public boolean isUserInRole(String role) {
				// String username = requestContext.getHeaders().getFirst(
				// "username");
				// Collection<String> userRoles =
				// UserManager.getUserRoles(username);
				Collection<String> userRoles = new ArrayList<String>() {
					private static final long serialVersionUID = 1L;
					{
						add(GROUP_USER);
						add(GROUP_ADMIN);
					}
				};

				switch (role) {
				case GROUP_USER:
					return userRoles.contains(GROUP_USER);
				case GROUP_ADMIN:
					return userRoles.contains(GROUP_ADMIN);
				default:
					return false;
				}
			}

			@Override
			public boolean isSecure() {
				return wrappedSecurityContext.isSecure();
			}

			@Override
			public Principal getUserPrincipal() {
				return wrappedSecurityContext.getUserPrincipal();
			}

			@Override
			public String getAuthenticationScheme() {
				return wrappedSecurityContext.getAuthenticationScheme();
			}
		});
	}
}
