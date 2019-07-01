package com.erommel;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.ApplicationPath;
import java.util.logging.Logger;

@ApplicationPath("/")
public class App extends ResourceConfig {
    public App() {
        Logger.getGlobal().info("Initializing App ResourceConfig...");
        register(JacksonFeature.class);

        packages("com.erommel.rest");
        property(ServerProperties.TRACING, "ALL");
        property(ServerProperties.TRACING_THRESHOLD, "TRACE");
    }
}