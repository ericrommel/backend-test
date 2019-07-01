package com.erommel.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqTransaction")
    @SequenceGenerator(name = "seqTransaction",
            sequenceName = "seq_transaction",
            allocationSize = 1)
    private long transaction_id;

    @JsonProperty("amount")
    @Column(nullable = false)
    private double amount;

    @JsonProperty("from")
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "from_account_fk"),
            name = "f_account_number", updatable = false)
    private Account fromAccount;

    @JsonProperty("to")
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "to_account_fk"),
            name = "t_account_number", updatable = false)
    private Account toAccount;

    @Column(nullable = false)
    private LocalDateTime dateTimeTransaction;

    public Transaction() {

    }

    public Transaction(Account fromAccount, Account toAccount, double amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.dateTimeTransaction = LocalDateTime.now();
    }

    public long getTransaction_id() {
        return transaction_id;
    }
    public void setTransaction_id(long transaction_id) {
        this.transaction_id = transaction_id;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Account getFromAccount() {
        return fromAccount;
    }
    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }
    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public LocalDateTime getDateTimeTransaction() {
        return dateTimeTransaction;
    }
    public void setDateTimeTransaction(LocalDateTime dateTimeTransaction) {
        this.dateTimeTransaction = dateTimeTransaction;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                ", from='" + fromAccount.getNumber() + '\'' +
                ", to='" + toAccount.getNumber() + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}