package com.erommel.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionRequest {
    @JsonProperty("from")
    private Long fromAccount;

    @JsonProperty("to")
    private Long toAccount;

    @JsonProperty("amount")
    private double amount;

    public Long getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Long fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Long getToAccount() {
        return toAccount;
    }

    public void setToAccount(Long toAccount) {
        this.toAccount = toAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
