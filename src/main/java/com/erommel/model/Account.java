package com.erommel.model;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqAccount")
    @SequenceGenerator(name = "seqAccount",
            sequenceName = "seq_account",
            allocationSize = 1)
    private long number;

    @Column(nullable = false, columnDefinition = "VARCHAR default 'EUR'")
    private String currency = "EUR";

    @Column(nullable = false, columnDefinition = "NUMBER default 0")
    private double balance;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "client_fk"),
            name = "client_id")
    private Client client;

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Account{" +
                "number=" + number +
                ", balance=" + balance +
                ", client=" + client +
                '}';
    }
}