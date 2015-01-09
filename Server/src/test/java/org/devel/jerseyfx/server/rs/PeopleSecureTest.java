package org.devel.jerseyfx.server.rs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;

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
import javax.ws.rs.core.UriBuilder;

import org.devel.jerseyfx.common.model.Person;
import org.devel.jerseyfx.server.filter.AuthorizationRequestFilter;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.junit.Test;

public class PeopleSecureTest extends GrizzlyTest {

	private static final String TEST_E_MAIL = "stefan.illgen@mail.com";
	
	static {
	    //for localhost testing only
	    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
	    new javax.net.ssl.HostnameVerifier(){
 
	        public boolean verify(String hostname,
	                javax.net.ssl.SSLSession sslSession) {
	            if (hostname.equals("localhost")) {
	                return true;
	            }
	            return false;
	        }
	    });
	}

	protected HttpServer createServer() {
		URI baseUri = UriBuilder.fromUri("https://localhost/").port(443)
				.build();
		ResourceConfig config = new ResourceConfig(PeopleSecure.class,
				RolesAllowedDynamicFeature.class, AuthorizationRequestFilter.class);
		SSLEngineConfigurator sslConfig = new SSLEngineConfigurator(
				SslConfigurator
						.newInstance()
						.trustStoreFile(
								getClass().getResource(
										"/cert/truststore_server").getPath())
						.trustStorePassword("password")
						.keyStoreFile(
								getClass().getResource("/cert/keystore_server")
										.getPath()).keyPassword("password")
						.createSSLContext());
		sslConfig.setClientMode(false);
		return GrizzlyHttpServerFactory.createHttpServer(baseUri, config, true,
				sslConfig, false);
	}

	@Override
	protected WebTarget createWebTarget() {
		SslConfigurator sslConfigurator = SslConfigurator
				.newInstance()
				.trustStoreFile(
						getClass().getResource("/cert/truststore_client")
								.getPath())
				.trustStorePassword("password")
				.keyStoreFile(
						getClass().getResource("/cert/keystore_client")
								.getPath()).keyPassword("password");
		SSLContext sslContext = sslConfigurator.createSSLContext();
		Client client = ClientBuilder.newBuilder().sslContext(sslContext)
				.build();

		// Set credentials fixed.
		client.register(HttpAuthenticationFeature.basicBuilder()
				.nonPreemptive().credentials("stefan", "").build());

		return client.target("https://localhost/").path("/people");

		// // Create a trust manager that does not validate certificate chains
		// TrustManager[] trustAllCerts = new TrustManager[] { new
		// X509TrustManager() {
		//
		// @Override
		// public void checkClientTrusted(
		// java.security.cert.X509Certificate[] chain, String authType)
		// throws CertificateException {
		//
		// }
		//
		// @Override
		// public void checkServerTrusted(
		// java.security.cert.X509Certificate[] chain, String authType)
		// throws CertificateException {
		//
		// }
		//
		// @Override
		// public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		// return null;
		// }
		// } };
		//
		// try {
		// // Install the all-trusting trust manager
		// SSLContext sc = SSLContext.getInstance("SSL");
		// sc.init(null, trustAllCerts, new java.security.SecureRandom());
		// HttpsURLConnection
		// .setDefaultSSLSocketFactory(sc.getSocketFactory());
		//
		// // Create all-trusting host name verifier
		// HostnameVerifier allHostsValid = new HostnameVerifier() {
		// public boolean verify(String hostname, SSLSession session) {
		// return true;
		// }
		// };
		//
		// // Install the all-trusting host verifier
		// HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		//
		// Client client = ClientBuilder.newBuilder().sslContext(sc).build();
		//
		// // client.register(HttpAuthenticationFeature.basicBuilder()
		// // .nonPreemptive().credentials("user", "superSecretPassword")
		// // .build());
		//
		// return client.target("https://localhost/").path("/people");
		//
		// } catch (KeyManagementException e) {
		// e.printStackTrace();
		// } catch (NoSuchAlgorithmException e) {
		// e.printStackTrace();
		// }
		//
		// return null;

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
			Response response = target.request().accept(MediaType.APPLICATION_JSON)
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
