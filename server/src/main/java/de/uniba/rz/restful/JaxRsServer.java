package de.uniba.rz.restful;

import java.net.URI;
import java.util.Properties;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class JaxRsServer{
	private static Properties properties = Configuration.loadProperties();

	public void run() {
		String serverUri = properties.getProperty("serverUri");
		URI baseUri = UriBuilder.fromUri(serverUri).build();
		ResourceConfig config = ResourceConfig.forApplicationClass(ExamplesApi.class);
		JdkHttpServerFactory.createHttpServer(baseUri, config);
		System.out.println("Server ready to serve your JAX-RS requests...");
	}
}
