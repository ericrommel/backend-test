package com.erommel.rest;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(ClientResource.class);
    }

    @Test
    public void testAddClient_Ok() {
        Response response = target("clients").request()
                .post(Entity.json(
                        "{\n" +
                        "\t\"name\": \"Amanda Another\",\n" +
                        "\t\"document_id\": \"1123485671\"\n" +
                        "}"
                ));

        assertEquals(
                "Http Response should be 201 ",
                Response.Status.CREATED.getStatusCode(),
                response.getStatus()
        );
    }

    @Test
    public void testAddClient_WithSameDocumentId() {
        Response response = target("clients").request()
                .post(Entity.json(
                        "{\n" +
                        "\t\"name\": \"Lucio Bence\",\n" +
                        "\t\"document_id\": \"23485671\"\n" +
                        "}"
                ));

        assertEquals(
                "Http Response should be 409 (Conflict)",
                Response.Status.CONFLICT.getStatusCode(),
                response.getStatus()
        );
    }

    @Test
    public void testGetClients() {
        Response response = target("clients").request().get();

        assertEquals(
                "Http Response should return status 200: ",
                Response.Status.OK.getStatusCode(),
                response.getStatus()
        );

        assertNotNull(
                "Http Response should return list",
                response.getEntity()
        );

        assertEquals(
                "Http Content-Type should be: ",
                MediaType.APPLICATION_JSON,
                response.getHeaderString(HttpHeaders.CONTENT_TYPE)
        );

        String content = response.readEntity(String.class);
        assertEquals(
                "Content of response is: ",
                "{\"result\":[{\"id\":1,\"name\":\"Amanda Bertullite\",\"document_id\":\"23485671\"},{\"id\":2,\"name\":\"Amanda Another\",\"document_id\":\"1123485671\"}]}",
                content
        );
    }

    @Test
    public void testGetClient_NotExist() {
        Response response = target("clients/4").request().get();

        assertEquals(
                "Http Response should return status 404: ",
                Response.Status.NOT_FOUND.getStatusCode(),
                response.getStatus()
        );

        String content = response.readEntity(String.class);
        assertEquals(
                "Content of response is: ",
                "{\"message\":\"Client with id 4 not found\"}",
                content
        );
    }

    @Test
    public void testGetClient_Exist() {
        Response response = target("clients/1").request().get();

        assertEquals(
                "Http Response should return status 200: ",
                Response.Status.OK.getStatusCode(),
                response.getStatus()
        );

        String content = response.readEntity(String.class);
        assertEquals(
                "Content of response is: ",
                "{\"id\":1,\"name\":\"Amanda Bertullite\",\"document_id\":\"23485671\"}",
                content
        );
    }
}