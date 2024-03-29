package com.erommel.rest;

import com.erommel.exception.ClientHasAccountException;
import com.erommel.exception.EntityAlreadyExistsException;
import com.erommel.exception.EntityNotFoundException;
import com.erommel.model.Client;
import com.erommel.rest.response.CollectionResponse;
import com.erommel.rest.response.ErrorResponse;
import com.service.ClientService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/clients")
public class ClientResource {
    private static final Logger LOG = Logger.getLogger(ClientResource.class.getName());

    private ClientService service = new ClientService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients() {
        LOG.log(Level.INFO, "Receiving request without param. Return all clients");
        List<Client> clientList = service.findAll();
        return Response
                .ok()
                .entity(new CollectionResponse(clientList))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
        LOG.log(Level.INFO, "Receiving request with id param {0}", id);

        try {
            Client client = service.findById(Long.parseLong(id));
            return Response.ok().entity(client).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Client client) {
        LOG.log(Level.INFO, "Receiving request with id param {0}", client);
        try {
            service.save(client);
            return Response.created(new URI("/api/clients/" + client.getId())).build();
        } catch (EntityAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Client client) {
        try {
            client.setId(id);
            service.update(client);
            return Response.ok(client).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        try {
            service.delete(id);
            return Response.ok().build();
        } catch (ClientHasAccountException e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }
}