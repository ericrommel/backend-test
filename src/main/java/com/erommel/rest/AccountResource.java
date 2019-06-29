package com.erommel.rest;

import com.erommel.exception.EntityNotFoundException;
import com.erommel.exception.EntityNotValidException;
import com.erommel.model.Account;
import com.erommel.rest.request.AccountRequest;
import com.erommel.rest.response.CollectionResponse;
import com.erommel.rest.response.ErrorResponse;
import com.service.AccountService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/accounts")
public class AccountResource {

    private static final Logger LOG = Logger.getLogger(AccountResource.class.getName());

    private AccountService service = new AccountService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccounts() {
        LOG.log(Level.INFO, "Receiving request without param. Return all accounts");
        List<Account> accountList = service.findAll();
        return Response
                .ok()
                .entity(new CollectionResponse(accountList))
                .build();
    }

    @Path("/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response get(@PathParam("accountNumber") Long accountNumber) {
        try {
            LOG.log(Level.INFO, "getting account number {0}", accountNumber);
            Account account = service.findByNumber(accountNumber);
            return Response.ok(account).build();
        } catch (EntityNotFoundException e) {
            LOG.log(Level.WARNING, "not found", e);
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "unexpected", e);
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response add(AccountRequest accountRequest) {
        try {
            Account account = service.save(accountRequest);
            return Response.created(new URI("/api/accounts/" + account.getNumber())).build();
        } catch (EntityNotValidException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "unexpected", e);
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }
}