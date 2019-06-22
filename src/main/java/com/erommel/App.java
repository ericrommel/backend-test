package com.erommel;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class App extends ResourceConfig {
    public App() {
        property(ServerProperties.TRACING, "ON_DEMAND");
        property(ServerProperties.TRACING_THRESHOLD, "SUMMARY");
    }
}