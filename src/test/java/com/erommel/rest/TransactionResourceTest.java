package com.erommel.rest;

import com.erommel.model.Client;
import com.erommel.model.Transaction;
import com.erommel.rest.request.AccountRequest;
import com.erommel.rest.request.TransactionRequest;
import com.erommel.rest.response.CollectionResponse;
import com.erommel.rest.response.ErrorResponse;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import java.time.LocalDate;

import static javax.ws.rs.core.Response.Status.*;
import static org.eclipse.jetty.http.MultiPartParser.LOG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransactionResourceTest extends JerseyTest {

    AccountRequest account1;
    AccountRequest account2;

    @Override
    protected Application configure() {
        return new ResourceConfig(TransactionResource.class)
                .register(ClientResource.class)
                .register(AccountResource.class);
    }

    @Before
    public void init() {
        LOG.info("Init tests for account...");

        /* Creating clients because these are necessary to add accounts */
        target("clients").request()
                .post(Entity.json((new Client(1L, "Eric Rommel", "123456"))));
        target("clients").request()
                .post(Entity.json(new Client(2L, "Caio Dantas", "987654")));

        /* Creating accounts using the clients added */
        AccountRequest accountRequest1 = createAccountRequest(1L);
        target("accounts").request()
                .post(Entity.json(accountRequest1));

        AccountRequest accountRequest2 = createAccountRequest(2L);
        target("accounts").request()
                .post(Entity.json(accountRequest2));
    }

    @Test
    public void testAddTransaction() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, 2L, 35.0);
        Response response = target("transactions/transfers").request()
                .post(Entity.json(transactionRequest));

        assertEquals(
                "Http Response should be 201 ",
                CREATED.getStatusCode(),
                response.getStatus()
        );
    }

    @Test
    public void testAddTransaction_AccountsAreTheSame() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, 1L, 35.0);
        Response response = target("transactions/transfers").request()
                .post(Entity.json(transactionRequest));
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should be 400 ",
                BAD_REQUEST.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "The origin and destination account numbers are the same",
                errorResponse.getMessage()
        );
    }

    @Test
    public void testAddTransaction_AmountIsNegative() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, 2L, -35.0);
        Response response = target("transactions/transfers").request()
                .post(Entity.json(transactionRequest));
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should be 400 ",
                BAD_REQUEST.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Transactions with amount equal or less than 0 are not permitted",
                errorResponse.getMessage()
        );
    }

    @Test
    public void testAddTransaction_AmountIsZero() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, 2L, 0.0);
        Response response = target("transactions/transfers").request()
                .post(Entity.json(transactionRequest));
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should be 400 ",
                BAD_REQUEST.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Transactions with amount equal or less than 0 are not permitted",
                errorResponse.getMessage()
        );
    }

    @Test
    public void testAddTransaction_DoesNotHaveMoneyEnough() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, 2L, 10000.0);
        Response response = target("transactions/transfers").request()
                .post(Entity.json(transactionRequest));
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should be 400 ",
                BAD_REQUEST.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Transaction rejected. From account does not have money enough",
                errorResponse.getMessage()
        );
    }

    @Test
    public void testAddTransaction_FromAccountDoesNotExist() {
        TransactionRequest transactionRequest = createTransactionRequest(100L, 2L, 35.0);
        Response response = target("transactions/transfers").request()
                .post(Entity.json(transactionRequest));
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should be 404 ",
                NOT_FOUND.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Account with id 100 not found",
                errorResponse.getMessage()
        );
    }

    @Test
    public void testAddTransaction_toAccountDoesNotExist() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, 100L, 35.0);
        Response response = target("transactions/transfers").request()
                .post(Entity.json(transactionRequest));
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should be 404 ",
                NOT_FOUND.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Account with id 100 not found",
                errorResponse.getMessage()
        );
    }

    @Test
    public void testGetTransfers() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, 2L, 35.0);
        target("transactions/transfers").request().post(Entity.json(transactionRequest));
        Response response = target("transactions/transfers").request().get();
        CollectionResponse collectionResponse = response.readEntity(CollectionResponse.class);

        assertEquals(
                "Http Response should return status 200: ",
                OK.getStatusCode(),
                response.getStatus()
        );

        assertFalse(
                "Should have at least 1 transaction made",
                collectionResponse.getResult().isEmpty()
        );
    }

    @Test
    public void testGetTransfer_Ok() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, 2L, 35.0);
        target("transactions/transfers").request().post(Entity.json(transactionRequest));
        Response response = target("transactions/transfers/1").request().get();

        assertEquals(
                "Http Response should return status 200: ",
                OK.getStatusCode(),
                response.getStatus()
        );

        Transaction content = response.readEntity(Transaction.class);
        assertEquals(
                "Content of response is: ",
                1,
                content.getTransaction_id()
        );

//        assertEquals(
//                "Content of response is: ",
//                1,
//                content.getTransaction_id()
//        );
    }

    @Test
    public void testGetTransfer_NotFount() {
        Response response = target("transactions/transfers/600").request().get();
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should return status 404: ",
                Response.Status.NOT_FOUND.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Transaction with id 600 not found",
                errorResponse.getMessage()
        );
    }

    @Test
    public void testGetTransfer_FromAccount() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, 2L, 35.0);
        target("transactions/transfers").request().post(Entity.json(transactionRequest));
        Response response = target("transactions/transfers/account/1").request().get();

        assertEquals(
                "Http Response should return status 200: ",
                Response.Status.OK.getStatusCode(),
                response.getStatus()
        );

        Transaction content = response.readEntity(Transaction.class);
        assertEquals(
                "Content of response is: ",
                1,
                content.getFromAccount().getNumber()
        );
    }

    @Test
    public void testGetTransfer_FromDate() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, 2L, 35.0);
        target("transactions/transfers").request().post(Entity.json(transactionRequest));
        Response response = target("transactions/transfers/from/" + LocalDate.now()).request().get();
        CollectionResponse collectionResponse = response.readEntity(CollectionResponse.class);

        assertEquals(
                "Http Response should return status 200: ",
                OK.getStatusCode(),
                response.getStatus()
        );

        assertFalse(
                "Should have at least 1 transaction made",
                collectionResponse.getResult().isEmpty()
        );
    }

    private AccountRequest createAccountRequest(Long clientId) {
        AccountRequest account = new AccountRequest();
        account.setClientId(clientId);
        account.setBalance(100);
        return account;
    }
    private TransactionRequest createTransactionRequest(Long fromAccountNumber, Long toAccountNumber, double amount) {
        TransactionRequest transaction = new TransactionRequest();
        transaction.setFromAccount(fromAccountNumber);
        transaction.setToAccount(toAccountNumber);
        transaction.setAmount(amount);
        return transaction;
    }
}