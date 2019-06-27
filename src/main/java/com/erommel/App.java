package com.erommel;

import com.erommel.model.Account;
import com.erommel.model.Client;
import com.erommel.repository.AccountRepository;
import com.erommel.repository.ClientRepository;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.ApplicationPath;
import java.util.logging.Logger;

@ApplicationPath("/")
public class App extends ResourceConfig {
    public App() {
        Logger.getGlobal().info("Initializing App ResourceConfig...");
        register(JacksonFeature.class);

        packages("com.erommel.rest");
        property(ServerProperties.TRACING, "ALL");
        property(ServerProperties.TRACING_THRESHOLD, "TRACE");

//        Client client1 = new Client();
//        client1.setName("Jurema Silva");
//        client1.setDocumentId("1234567");
//        Client client2 = new Client();
//        client2.setName("Fulano Sicrano");
//        client2.setDocumentId("123456");
//
//        ClientRepository clientRepository = new ClientRepository();
//        clientRepository.save(client1);
//        clientRepository.save(client2);
//
//        Account account1 = new Account();
//        account1.setClient(client1);
//        account1.setBalance(50.0);
//        Account account2 = new Account();
//        account2.setClient(client1);
//        account2.setBalance(70.0);
//
//        AccountRepository accountRepository = new AccountRepository();
//        accountRepository.save(account1);
//        accountRepository.save(account2);

    }
}