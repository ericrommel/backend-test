package com.erommel.rest;

import com.erommel.model.Client;
import com.erommel.rest.request.AccountRequest;
import com.erommel.rest.response.CollectionResponse;
import com.erommel.rest.response.ErrorResponse;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(ClientResource.class).register(AccountResource.class);
    }

    @Test
    public void testAddClient_Ok() {
        Response response = target("clients").request()
                .post(Entity.json(new Client("Eric Rommel", "123456")));

        assertEquals(
                "Http Response should be 201 ",
                CREATED.getStatusCode(),
                response.getStatus()
        );
    }

    @Test
    public void testAddClient_WithSameDocumentId() {
        Response response = target("clients").request()
                .post(Entity.json(new Client("Cauan Liam", "987654")));

        assertEquals(
                "Http Response should be 201",
                CREATED.getStatusCode(),
                response.getStatus()
        );

        response = target("clients").request()
                .post(Entity.json(new Client("Caio Dantas", "987654")));
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should be 409",
                CONFLICT.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Client{name='Caio Dantas', documentId='987654'} already exists",
                errorResponse.getMessage()
        );
    }

    @Test
    public void testGetClients() {
        Response response;

        response = target("clients").request()
                .post(Entity.json(new Client("Eric Rommel", "123456")));

        response = target("clients").request().get();
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
    public void testGetClient_NotExist() {
        Response response = target("clients/100000").request().get();
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should return status 404: ",
                NOT_FOUND.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Client with id 100000 not found",
                errorResponse.getMessage()
        );
    }

    @Test
    public void testGetClient_Ok() {
        Response response;

        response = target("clients").request()
                .post(Entity.json(new Client("Eric Rommel", "123456")));
        response = target("clients/1").request().get();

        assertEquals(
                "Http Response should return status 200: ",
                OK.getStatusCode(),
                response.getStatus()
        );

        Client content = response.readEntity(Client.class);
        assertEquals(
                "Content of response is: ",
                1,
                content.getId()
        );
    }

    @Test
    public void testUpdateClient_Name() {
        Client client = new Client("Eric Rommel", "123456");

        target("clients").request()
                .post(Entity.json(client));

        client.setName("Caio Dantas");
        Response response = target("clients/1").request().put(Entity.json(client));

        assertEquals(
                "Http Response should return status 200: ",
                OK.getStatusCode(),
                response.getStatus()
        );

        Client content = response.readEntity(Client.class);
        assertEquals(
                "Content of response is: ",
                "Caio Dantas",
                content.getName()
        );
    }

    @Test
    public void testUpdateClient_DocumentId() {
        Client client = new Client("Eric Rommel", "123456");

        Response response;

        response = target("clients").request()
                .post(Entity.json(client));

        client.setDocumentId("987654");
        response = target("clients/1").request().put(Entity.json(client));

        assertEquals(
                "Http Response should return status 200: ",
                OK.getStatusCode(),
                response.getStatus()
        );

        Client content = response.readEntity(Client.class);
        assertEquals(
                "Content of response is: ",
                "987654",
                content.getDocumentId()
        );
    }

    @Test
    public void testDeleteClient_WithoutAccountRegistered() {
        Response response;

        response = target("clients").request()
                .post(Entity.json(new Client("Eric Rommel", "123456")));

        response = target("clients").request()
                .post(Entity.json(new Client("Sandra Alves", "765983")));

        response = target("clients/2").request().get();
        assertEquals(
                "Http Response should return status 200: ",
                OK.getStatusCode(),
                response.getStatus()
        );

        target("clients/2").request().delete();
        assertEquals(
                "Http Response should return status 200: ",
                OK.getStatusCode(),
                response.getStatus()
        );

        response = target("clients/2").request().get();
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should return status 404: ",
                NOT_FOUND.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Client with id 2 not found",
                errorResponse.getMessage()
        );
    }

    @Test
    public void testDeleteClient_WithAccountRegistered() {
        Response response;

        response = target("clients").request()
                .post(Entity.json(new Client("Eric Rommel", "123456")));

        AccountRequest account = new AccountRequest();
        account.setClientId(1L);
        account.setBalance(100.0);
        response = target("accounts/").request()
                .post(Entity.json(account));

        assertEquals(
                "Http Response should be 201 ",
                CREATED.getStatusCode(),
                response.getStatus()
        );

        response = target("clients/1").request().delete();
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);

        assertEquals(
                "Http Response should return status 417: ",
                EXPECTATION_FAILED.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Content of response is: ",
                "Client with id 1 has one or more accoutns registered.",
                errorResponse.getMessage()
        );
    }
}