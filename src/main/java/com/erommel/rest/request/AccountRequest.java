package com.erommel.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountRequest {
    @JsonProperty("client_id")
    private Long clientId;

    private double balance;
    private Long number;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

}