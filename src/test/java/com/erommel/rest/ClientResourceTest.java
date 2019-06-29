package com.erommel.rest;

import com.erommel.model.Client;
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
        return new ResourceConfig(ClientResource.class);
    }

    @Test
    public void testAddClient_Ok() {
        Response response = target("clients").request()
                .post(Entity.json(new Client(1L, "Eric Rommel", "123456")));

        assertEquals(
                "Http Response should be 201 ",
                CREATED.getStatusCode(),
                response.getStatus()
        );
    }

    @Test
    public void testAddClient_WithSameDocumentId() {
        Response response = target("clients").request()
                .post(Entity.json(new Client(2L, "Cauan Liam", "987654")));

        assertEquals(
                "Http Response should be 201",
                CREATED.getStatusCode(),
                response.getStatus()
        );

        response = target("clients").request()
                .post(Entity.json(new Client(3L, "Caio Dantas", "987654")));

        assertEquals(
                "Http Response should be 409",
                CONFLICT.getStatusCode(),
                response.getStatus()
        );
    }

    @Test
    public void testGetClients() {
        target("clients").request()
                .post(Entity.json(new Client(1L, "Eric Rommel", "123456")));

        Response response = target("clients").request().get();
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
    public void testGetClient_Exist() {
        target("clients").request()
                .post(Entity.json(new Client(1L, "Eric Rommel", "123456")));

        Response response = target("clients/1").request().get();

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
}