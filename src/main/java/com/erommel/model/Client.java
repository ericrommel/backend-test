package com.erommel.model;

import java.util.List;

public class Client {

    private long id;
    private Account account;
    private String name;
    private List<Client> clients;

    public Client() {

    }

    public Client(String name) {
        this.name = name;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void addClients(Client client) {
        this.clients.add(client);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
