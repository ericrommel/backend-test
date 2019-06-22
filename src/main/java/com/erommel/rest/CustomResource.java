package com.erommel.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/custom")
public class CustomResource {

    private static final Logger LOG = Logger.getLogger(CustomResource.class.getName());

    public CustomResource() {
        LOG.fine("Custom resources started...");
    }

    @GET
    @Path("/{name}")
    public Response get(@PathParam("name") String name) {
        LOG.log(Level.INFO, "Receiving request with path param {}", name);
        return Response
                .ok()
                .entity("This name " + name + " has " + name.length() + " chars")
                .build();
    }
}