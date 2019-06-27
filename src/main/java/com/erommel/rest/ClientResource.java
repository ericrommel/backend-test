package com.erommel.rest;

import com.erommel.exception.EntityAlreadyExistsException;
import com.erommel.exception.EntityNotFoundException;
import com.erommel.model.Client;
import com.erommel.rest.response.CollectionResponse;
import com.erommel.rest.response.ErrorResponse;
import com.service.ClientService;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/clients")
@Api(value = "/clients", description = "Manage clients. As these data are sensible, the DELETE operation is not allowed.")
public class ClientResource {
    private static final Logger LOG = Logger.getLogger(ClientResource.class.getName());

    private ClientService service = new ClientService();

    @GET
    @ApiOperation(value = "Returns all clients registered",
            response = Client.class,
            responseContainer = "List")
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
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid ID supplied",
                    responseHeaders = @ResponseHeader(name = "X-Rack-Cache",
                    description = "Explains whether or not a cache was used",
                    response = Boolean.class)),
            @ApiResponse(code = 404, message = "Client not found") })
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response get(
            @ApiParam( value = "Page to fetch", required = true )
            @PathParam("id") String id) {
        LOG.log(Level.INFO, "Receiving request with id param {}", id);

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
        try {
            service.save(client);
            return Response.created(new URI("/api/clients/" + client.getId())).build();
        } catch (EntityAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }
}