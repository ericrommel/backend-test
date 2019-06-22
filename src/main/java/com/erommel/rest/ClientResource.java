package com.erommel.rest;

import com.erommel.model.Client;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/clients")
public class ClientResource {
    private static final Logger LOG = Logger.getLogger(ClientResource.class.getName());

    @GET
    @Path("/")
    public Response getClients() {
        LOG.log(Level.INFO, "Receiving request without param. Return all clients");
        Client client = new Client();
        return Response
                .ok()
                .entity("Clients: " + client.getClients())
                .build();
    }

    @GET
    @Path("/{name}")
    public Response get(@PathParam("name") String name) {
        LOG.log(Level.INFO, "Receiving request with path param {}", name);
        Client c = new Client(name);
        return Response
                .ok()
                .entity("Client: " + c.getName() + ": " + c.getId())
                .build();
    }

    @POST
    @Path("/{name}")
    public Response add(@PathParam("name") String name) throws URISyntaxException {
        Client c = new Client(name);
        c.addClients(c);
        return Response.created(new URI("/model/clients/")).build();
    }
}