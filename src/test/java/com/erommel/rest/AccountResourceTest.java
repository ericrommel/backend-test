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

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(AccountResource.class).register(ClientResource.class);
    }

    @Before
    public void init() {
        /* Adding a client because this is necessary to add an account */
        Response response_client = target("clients").request()
                .post(Entity.json("{\n" +
                        "\t\"name\": \"Amanda Bertullite\",\n" +
                        "\t\"document_id\": \"23485671\"\n" +
                        "}"));
    }

    @Test
    public void testAddAccount_ClientNotFound() {
        Response response = target("accounts").request()
                .post(Entity.json("{\n" +
                        "\t\"client_id\": 4,\n" +
                        "\t\"balance\": 100\n" +
                        "}"));

        assertEquals(
                "Http Response should be 404 ",
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
    public void testAddAccount_Ok() {
        Response response_account = target("accounts/").request()
                .post(Entity.json("{\n" +
                        "\t\"client_id\": 1,\n" +
                        "\t\"balance\": 100\n" +
                        "}"));

        assertEquals(
                "Http Response should be 201 ",
                Response.Status.CREATED.getStatusCode(),
                response_account.getStatus()
        );

        String content_account = response_account.readEntity(String.class);
        assertEquals(
                "No body returned for response",
                "",
                content_account
        );
    }

    @Test
    public void testGetAccounts() {
        Response response = target("accounts").request().get();

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
                "{\"result\":[{\"number\":1,\"currency\":\"HUF\",\"balance\":100.0,\"client\":{\"id\":1,\"name\":\"Amanda Bertullite\",\"document_id\":\"23485671\"}}]}",
                content
        );
    }

    @Test
    public void testGetAccount() {
        Response response = target("accounts/1").request().get();

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
                "{\"number\":1,\"currency\":\"HUF\",\"balance\":100.0,\"client\":{\"id\":1,\"name\":\"Amanda Bertullite\",\"document_id\":\"23485671\"}}",
                content
        );
    }

    @Test
    public void testGetAccount_NotExist() {
        Response response = target("accounts/4").request().get();

        assertEquals(
                "Http Response should return status 404: ",
                Response.Status.NOT_FOUND.getStatusCode(),
                response.getStatus()
        );

        String content = response.readEntity(String.class);
        assertEquals(
                "Content of response is: ",
                "{\"message\":\"Account with id 4 not found\"}",
                content
        );
    }
}