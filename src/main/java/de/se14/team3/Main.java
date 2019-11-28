package de.se14.team3;

import org.apache.catalina.startup.Tomcat;

import java.util.Optional;

public class Main {

	private static final Optional<String> PORT = Optional.ofNullable(System.getenv("PORT"));
	private static final Optional<String> HOSTNAME = Optional.ofNullable(System.getenv("HOSTNAME"));

	public static void main(String[] args) throws Exception {
		String contextPath = "";
		String appBase = ".";
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(Integer.parseInt(PORT.orElse("8080")));
		tomcat.setHostname(HOSTNAME.orElse("localhost"));
		tomcat.getHost().setAppBase(appBase);
		tomcat.addWebapp(contextPath, appBase);
        tomcat.getConnector(); // Trigger the creation of the default connector
		tomcat.start();
		tomcat.getServer().await();
	}
}
