package com.erommel.rest;

import com.erommel.rest.request.AccountRequest;
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

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

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
        /* Adding clients because these are necessary to add accounts */
        Response response_client = target("clients").request()
                .post(Entity.json("{\n" +
                        "\t\"name\": \"Amanda Bertullite\",\n" +
                        "\t\"document_id\": \"23485671\"\n" +
                        "}"));

        response_client = target("clients").request()
                .post(Entity.json("{\n" +
                        "\t\"name\": \"David Silva\",\n" +
                        "\t\"document_id\": \"32348\"\n" +
                        "}"));


        /* After clients added, we can added account using the clients added */
        Response response_account = target("accounts/").request()
                .post(Entity.json("{\n" +
                        "\t\"client_id\": 1,\n" +
                        "\t\"balance\": 100\n" +
                        "}"));
        response_account = target("accounts/").request()
                .post(Entity.json("{\n" +
                        "\t\"client_id\": 2,\n" +
                        "\t\"balance\": 80\n" +
                        "}"));
    }

    @Test
    public void testAddTransaction() {
        Response response = target("transactions/transfers").request()
                .post(Entity.json("{\n" +
                        "\t\"from\": 1,\n" +
                        "\t\"to\": 2,\n" +
                        "\t\"amount\": 35\n" +
                        "}"));

        assertEquals(
                "Http Response should be 201 ",
                Response.Status.CREATED.getStatusCode(),
                response.getStatus()
        );

        String content = response.readEntity(String.class);
        assertEquals(
                "No body returned for response",
                "",
                content
        );
    }

    @Test
    public void testAddTransaction_AccountsAreTheSame() {
        Response response = target("transactions/transfers").request()
                .post(Entity.json("{\n" +
                        "\t\"from\": 1,\n" +
                        "\t\"to\": 1,\n" +
                        "\t\"amount\": 15\n" +
                        "}"));

        assertEquals(
                "Http Response should be 400 ",
                Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatus()
        );

        String content = response.readEntity(String.class);
        assertEquals(
                "Content of response is: ",
                "{\"message\":\"The origin and destination account numbers are the same\"}",
                content
        );
    }

    @Test
    public void testAddTransaction_AmountIsNegative() {
        Response response = target("transactions/transfers").request()
                .post(Entity.json("{\n" +
                        "\t\"from\": 1,\n" +
                        "\t\"to\": 2,\n" +
                        "\t\"amount\": 0\n" +
                        "}"));

        assertEquals(
                "Http Response should be 400 ",
                Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatus()
        );

        String content = response.readEntity(String.class);
        assertEquals(
                "Content of response is: ",
                "{\"message\":\"Transactions with amount equal or less than 0 are not permitted\"}",
                content
        );
    }

    @Test
    public void testAddTransaction_AmountIsZero() {
        Response response = target("transactions/transfers").request()
                .post(Entity.json("{\n" +
                        "\t\"from\": 1,\n" +
                        "\t\"to\": 2,\n" +
                        "\t\"amount\": -10\n" +
                        "}"));

        assertEquals(
                "Http Response should be 400 ",
                Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatus()
        );

        String content = response.readEntity(String.class);
        assertEquals(
                "Content of response is: ",
                "{\"message\":\"Transactions with amount equal or less than 0 are not permitted\"}",
                content
        );
    }

    @Test
    public void testAddTransaction_DoesNotHaveMoneyEnough() {
        Response response = target("transactions/transfers").request()
                .post(Entity.json("{\n" +
                        "\t\"from\": 1,\n" +
                        "\t\"to\": 2,\n" +
                        "\t\"amount\": 100000\n" +
                        "}"));

        assertEquals(
                "Http Response should be 400 ",
                Response.Status.BAD_REQUEST.getStatusCode(),
                response.getStatus()
        );

        String content = response.readEntity(String.class);
        assertEquals(
                "Content of response is: ",
                "{\"message\":\"Transaction rejected. From account does not have money enough\"}",
                content
        );
    }

    @Test
    public void testAddTransaction_FromAccountDoesNotExist() {
        Response response = target("transactions/transfers").request()
                .post(Entity.json("{\n" +
                        "\t\"from\": 100,\n" +
                        "\t\"to\": 1,\n" +
                        "\t\"amount\": 15\n" +
                        "}"));

        assertEquals(
                "Http Response should be 404 ",
                Response.Status.NOT_FOUND.getStatusCode(),
                response.getStatus()
        );

        String content = response.readEntity(String.class);
        assertEquals(
                "Content of response is: ",
                "{\"message\":\"Account with id 100 not found\"}",
                content
        );
    }

    @Test
    public void testAddTransaction_toAccountDoesNotExist() {
        Response response = target("transactions/transfers").request()
                .post(Entity.json("{\n" +
                        "\t\"from\": 1,\n" +
                        "\t\"to\": 100,\n" +
                        "\t\"amount\": 15\n" +
                        "}"));

        assertEquals(
                "Http Response should be 404 ",
                Response.Status.NOT_FOUND.getStatusCode(),
                response.getStatus()
        );

        String content = response.readEntity(String.class);
        assertEquals(
                "Content of response is: ",
                "{\"message\":\"Account with id 100 not found\"}",
                content
        );
    }

    @Test
    public void testGetTransfers() {
        Response response = target("transactions/transfers").request().get();

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
    }

    @Test
    public void testGetTransfer_Ok() {
        Response response = target("transactions/transfers/1").request().get();

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
    }

    @Test
    public void testGetTransfer_NotFount() {
        Response response = target("transactions/transfers/600").request().get();

        assertEquals(
                "Http Response should return status 404: ",
                Response.Status.NOT_FOUND.getStatusCode(),
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
                "{\"message\":\"Transaction with id 600 not found\"}",
                content
        );
    }

    @Test
    public void testGetTransfer_FromAccount() {
        Response response = target("transactions/transfers/account/1").request().get();

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
    }

    @Test
    public void testGetTransfer_FromDate() {
        Response response = target("transactions/transfers/from/" + LocalDate.now()).request().get();

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
    }
}