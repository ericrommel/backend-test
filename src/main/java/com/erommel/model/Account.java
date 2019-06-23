package com.erommel.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqAccount")
    @SequenceGenerator(name = "seqAccount",
            sequenceName = "seq_account",
            allocationSize = 1)
    private long number;

    @Column(nullable = false, columnDefinition = "NUMBER default 0")
    private String currency;

    @Column(nullable = false, columnDefinition = "NUMBER default 0")
    private double balance;

    @OneToMany(mappedBy = "account")
    private List<Client> clients;

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
}