package com.erommel.rest;

import com.erommel.model.Client;
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
                .post(Entity.json((new Client(1L, "Eric Rommel", "999888"))));
        target("clients").request()
                .post(Entity.json(new Client(2L, "Caio Dantas", "888999")));

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
        target("transactions/transfers").request()
                .post(Entity.json(transactionRequest));

        Response response = target("transactions/transfers/1").request().get();

        assertEquals(
                "Http Response should return status 200: ",
                OK.getStatusCode(),
                response.getStatus()
        );
    }

    @Test
    public void testGetTransfer_NotFound() {
        Response response = target("transactions/transfers/600").request().get();
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should return status 404: ",
                NOT_FOUND.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Transaction with id 600 not found",
                errorResponse.getMessage()
        );
    }

    @Test
    public void testGetTransfer_ByFromAccount() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, 2L, 35.0);
        target("transactions/transfers").request().post(Entity.json(transactionRequest));
        Response response = target("transactions/transfers/account/from/1").request().get();
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
    public void testGetTransfer_ByFromAccount_NotExist() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, 2L, 35.0);
        target("transactions/transfers").request().post(Entity.json(transactionRequest));
        Response response = target("transactions/transfers/account/from/2").request().get();
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should return status 404: ",
                NOT_FOUND.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Transaction from account number 2 not found",
                errorResponse.getMessage()
        );
    }

    @Test
    public void testGetTransfer_ByToAccount() {
        TransactionRequest transactionRequest = createTransactionRequest(1L, 2L, 35.0);
        target("transactions/transfers").request().post(Entity.json(transactionRequest));
        Response response = target("transactions/transfers/account/to/1").request().get();
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should return status 404: ",
                NOT_FOUND.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Transaction from account number 1 not found",
                errorResponse.getMessage()
        );
    }

    @Test
    public void testGetTransfer_FromValidDate() {
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

    @Test
    public void testGetTransfer_FromInvalidDate() {
        Response response = target("transactions/transfers/from/2019-02-30").request().get();
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should be 400 ",
                BAD_REQUEST.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Text '2019-02-30' could not be parsed: Invalid date 'FEBRUARY 30'",
                errorResponse.getMessage()
        );
    }

    @Test
    public void testGetTransfer_FromFutureDate() {
        LocalDate date = LocalDate.now().plusDays(10);
        Response response = target("transactions/transfers/from/" + date).request().get();
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should be 404 ",
                NOT_FOUND.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Transactions from " + date + " not found",
                errorResponse.getMessage()
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