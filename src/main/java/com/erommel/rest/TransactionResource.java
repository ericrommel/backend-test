package com.erommel.rest;

import com.erommel.exception.EntityNotFoundException;
import com.erommel.exception.EntityNotValidException;
import com.erommel.model.Account;
import com.erommel.model.Client;
import com.erommel.model.Transaction;
import com.erommel.rest.request.AccountRequest;
import com.erommel.rest.request.TransactionRequest;
import com.erommel.rest.response.CollectionResponse;
import com.erommel.rest.response.ErrorResponse;
import com.service.TransactionService;
import javafx.util.converter.LocalDateStringConverter;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/transactions")
public class TransactionResource {

    private static final Logger LOG = Logger.getLogger(TransactionResource.class.getName());

    private TransactionService service = new TransactionService();

    @Path("/transfers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response add(TransactionRequest transactionRequest) {
        try {
            Transaction transaction = service.save(transactionRequest);
            return Response.created(new URI("/api/transactions/" + transaction.getId())).build();
        } catch (EntityNotValidException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "unexpected", e);
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/transfers")
    public Response getTransfers() {
        LOG.log(Level.INFO, "Receiving request without param. Return all transfers");
        List<Transaction> transactionList = service.findAll();
        return Response
                .ok()
                .entity(new CollectionResponse(transactionList))
                .build();
    }

    @Path("/transfers/transfer/{transactionId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response get(@PathParam("transactionId") Long transactionId) {
        try {
            Transaction transaction = service.findById(transactionId);
            return Response.ok(transaction).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "unexpected", e);
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @Path("/transfers/from/{dateTimeTransaction}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response get(@PathParam("dateTimeTransaction") String dateTimeTransaction) {
        LocalDate date = null;
        try {
            date = LocalDate.parse(dateTimeTransaction);
        } catch(Exception e) {
            e.getMessage();
        }

        try {
            List<Transaction> transactions = service.findByDate(date);
            return Response.ok(transactions).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "unexpected", e);
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @Path("/transfers/account/{fromAccount}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getFromAccount(@PathParam("fromAccount") Long fromAccount) {
        try {
            Transaction transaction = service.findByFromAccount(fromAccount);
            return Response.ok(transaction).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "unexpected", e);
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }
}