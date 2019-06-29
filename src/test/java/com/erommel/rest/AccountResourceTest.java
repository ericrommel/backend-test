package com.erommel.rest;

import com.erommel.model.Account;
import com.erommel.model.Client;
import com.erommel.rest.request.AccountRequest;
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
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountResourceTest extends JerseyTest {

    private Logger LOG = Logger.getLogger(AccountResourceTest.class.getName());

    @Override
    protected Application configure() {
        return new ResourceConfig(AccountResource.class).register(ClientResource.class);
    }

    @Before
    public void init() {
        LOG.info("Init tests for account...");

        Client client = new Client();
        client.setName("Amanda Bertullite");
        client.setDocumentId("23485671");

        /* Adding a client because this is necessary to add an account */
        target("clients").request()
                .post(Entity.json(client));
    }

    @Test
    public void testAddAccount_ClientNotFound() {
        AccountRequest accountRequest = createAccountRequest(1000000);

        Response response = target("accounts").request()
                .post(Entity.json(accountRequest));

        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should be 404 ",
                NOT_FOUND.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Client with id 1000000 not found",
                errorResponse.getMessage()
        );
    }

    @Test
    public void testAddAccount_Ok() {
        AccountRequest accountRequest = createAccountRequest(1);

        Response response = target("accounts/").request()
                .post(Entity.json(accountRequest));

        assertEquals(
                "Http Response should be 201 ",
                CREATED.getStatusCode(),
                response.getStatus()
        );
    }

    @Test
    public void testGetAccounts() {
        AccountRequest accountRequest = createAccountRequest(1);

        target("accounts").request().post(Entity.json(accountRequest));
        Response response = target("accounts").request().get();
        CollectionResponse collectionResponse = response.readEntity(CollectionResponse.class);

        assertEquals(
                "Http Response should return status 200: ",
                OK.getStatusCode(),
                response.getStatus()
        );

        assertFalse(
                "Should have at least 1 account registered",
                collectionResponse.getResult().isEmpty()
        );
    }

    @Test
    public void testGetAccount() {
        AccountRequest accountRequest = createAccountRequest(1);
        target("accounts/").request()
                .post(Entity.json(accountRequest));

        Response response = target("accounts/1").request().get();

        assertEquals(
                "Http Response should return status 200: ",
                OK.getStatusCode(),
                response.getStatus()
        );

        Account content = response.readEntity(Account.class);
        assertEquals(
                "Content of response is: ",
                1,
                content.getNumber()
        );
    }

    @Test
    public void testGetAccount_NotExist() {
        Response response = target("accounts/100000").request().get();
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should return status 404: ",
                NOT_FOUND.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Account with id 100000 not found",
                errorResponse.getMessage()
        );
    }

    private AccountRequest createAccountRequest(long clientId) {
        AccountRequest account = new AccountRequest();
        account.setClientId(clientId);
        account.setBalance(100);
        return account;
    }
}