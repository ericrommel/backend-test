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
    private long id;

    @JsonProperty("amount")
    @Column(unique = false, nullable = false)
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

    @Column(unique = false, nullable = false)
    private LocalDateTime dateTimeTransaction;

    public Transaction() {

    }

    public Transaction(Account fromAccount, Account toAccount, double amount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.dateTimeTransaction = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Transaction transaction = (Transaction) o;
//
//        if (id != transaction.id) return false;
//        return documentId.equals(transaction documentId);
//    }
//
//    @Override
//    public int hashCode() {
//        int result = (int) (id ^ (id >>> 32));
//        result = 31 * result + documentId.hashCode();
//        return result;
//    }
//
    @Override
    public String toString() {
        return "Transaction{" +
                ", from='" + fromAccount.getNumber() + '\'' +
                ", to='" + toAccount.getNumber() + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}