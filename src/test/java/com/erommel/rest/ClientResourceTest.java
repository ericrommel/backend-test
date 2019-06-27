package com.erommel.rest;

import org.json.*;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
        Response response;

        response = target("clients").request()
                .post(Entity.json("{\n" +
                                "\t\"name\": \"Julius Maximus\",\n" +
                                "\t\"document_id\": \"987654321\"\n" +
                                "}"
                ));

        assertEquals(
                "Http Response should be 201",
                Response.Status.CREATED.getStatusCode(),
                response.getStatus()
        );

        response = target("clients").request()
                .post(Entity.json("{\n" +
                        "\t\"name\": \"Maximus Julius\",\n" +
                        "\t\"document_id\": \"987654321\"\n" +
                        "}"
                ));
        assertEquals(
                "Http Response should be 409",
                Response.Status.CONFLICT.getStatusCode(),
                response.getStatus()
        );
    }

    @Test
    public void testGetClients() {
        Response response;
        response = target("clients").request()
                .post(Entity.json(
                        "{\n" +
                        "\t\"name\": \"Eric Dantas\",\n" +
                        "\t\"document_id\": \"666888\"\n" +
                        "}"
                ));

        response = target("clients").request().get();
        assertEquals(
                "Http Response should return status 200: ",
                Response.Status.OK.getStatusCode(),
                response.getStatus()
        );

        assertEquals(
                "Http Content-Type should be: ",
                MediaType.APPLICATION_JSON,
                response.getHeaderString(HttpHeaders.CONTENT_TYPE)
        );

        String content = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(content);
        JSONArray result = (JSONArray) jsonObject.get("result");
        assertFalse(
                "Should have at least 1 client registered",
                result.isEmpty()
        );
    }

    @Test
    public void testGetClient_NotExist() {
        Response response = target("clients/100000").request().get();

        assertEquals(
                "Http Response should return status 404: ",
                Response.Status.NOT_FOUND.getStatusCode(),
                response.getStatus()
        );

        String content = response.readEntity(String.class);
        assertEquals(
                "Content of response is: ",
                "{\"message\":\"Client with id 100000 not found\"}",
                content
        );
    }

    @Test
    public void testGetClient_Exist() {
        Response response;
        response = target("clients").request()
                .post(Entity.json(
                        "{\n" +
                        "\t\"name\": \"Rommel Dantas\",\n" +
                        "\t\"document_id\": \"777888\"\n" +
                        "}"
                ));

        response = target("clients/1").request().get();

        assertEquals(
                "Http Response should return status 200: ",
                Response.Status.OK.getStatusCode(),
                response.getStatus()
        );

        String content = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(content);
        assertEquals(
                "Content of response is: ",
                1,
                jsonObject.get("id")
        );
    }
}