package com.erommel.rest;

import com.erommel.exception.EntityNotFoundException;
import com.erommel.exception.EntityNotValidException;
import com.erommel.exception.TransactionNotFoundException;
import com.erommel.exception.TransactionNotValidException;
import com.erommel.model.Transaction;
import com.erommel.rest.request.TransactionRequest;
import com.erommel.rest.response.CollectionResponse;
import com.erommel.rest.response.ErrorResponse;
import com.service.TransactionService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
            return Response.created(new URI("/api/transactions/" + transaction.getTransaction_id())).build();
        } catch (EntityNotValidException | TransactionNotValidException e) {
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

    @Path("/transfers/{transactionId}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response get(@PathParam("transactionId") Long transactionId) {
        LOG.log(Level.INFO, "Getting transaction id " + transactionId);
        try {
            Transaction transaction = service.findById(transactionId);
            return Response.ok().entity(transaction).build();
        } catch (TransactionNotFoundException e) {
            LOG.log(Level.WARNING, "Transactions not found.", e.getMessage());
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
        LOG.log(Level.INFO, "Getting all transfers from date: " + dateTimeTransaction);
        try {
            LocalDate date = LocalDate.parse(dateTimeTransaction, DateTimeFormatter.ISO_LOCAL_DATE);
            List<Transaction> transactions = service.findByDate(date);
            return Response
                    .ok()
                    .entity(new CollectionResponse(transactions))
                    .build();
        } catch (DateTimeParseException e) {
            LOG.log(Level.WARNING, "Conversion of date " + dateTimeTransaction + " using ISO date time failed.", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getMessage())).build();
        } catch (TransactionNotFoundException e) {
            LOG.log(Level.WARNING, "Transactions not found.", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "unexpected", e.getMessage());
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @Path("/transfers/account/from/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getByFromAccount(@PathParam("accountNumber") Long accountNumber) {
        LOG.log(Level.INFO, "Getting all transfers by from account number " + accountNumber);
        try {
            List<Transaction> transactions = service.findByFromAccount(accountNumber);
            return Response
                    .ok()
                    .entity(new CollectionResponse(transactions))
                    .build();
        } catch (TransactionNotFoundException e) {
            LOG.log(Level.WARNING, "Transactions not found", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "unexpected", e);
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }

    @Path("/transfers/account/to/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getByToAccount(@PathParam("accountNumber") Long accountNumber) {
        LOG.log(Level.INFO, "Getting all transfers by to account number " + accountNumber);
        try {
            List<Transaction> transactions = service.findByToAccount(accountNumber);
            return Response
                    .ok()
                    .entity(new CollectionResponse(transactions))
                    .build();
        } catch (TransactionNotFoundException e) {
            LOG.log(Level.WARNING, "Transactions not found", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorResponse(e.getMessage())).build();
        } catch (Exception e) {
            LOG.log(Level.WARNING, "unexpected", e);
            return Response.serverError().entity(new ErrorResponse(e.getMessage())).build();
        }
    }
}