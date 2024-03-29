package com.service;

import com.erommel.exception.EntityNotValidException;
import com.erommel.exception.TransactionNotFoundException;
import com.erommel.exception.TransactionNotValidException;
import com.erommel.model.Account;
import com.erommel.model.Transaction;
import com.erommel.repository.TransactionRepository;
import com.erommel.rest.request.TransactionRequest;

import javax.ws.rs.BadRequestException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionService {
    private static final Logger LOG = Logger.getLogger(AccountService.class.getName());

    private TransactionRepository repository;
    private AccountService accountService;

    public TransactionService() {
        this.repository = new TransactionRepository();
        this.accountService = new AccountService();
    }

    public Transaction save(TransactionRequest transactionRequest) throws BadRequestException {
        validateTransaction(transactionRequest);

        Account fromAccount = accountService.findByNumber(transactionRequest.getFromAccount());
        Account toAccount = accountService.findByNumber(transactionRequest.getToAccount());

        double discountBalance = fromAccount.getBalance() - transactionRequest.getAmount();
        if (discountBalance <= 0) {
            throw new TransactionNotValidException("Transaction rejected. From account does not have money enough");
        }
        double additionBalance = toAccount.getBalance() + transactionRequest.getAmount();
        fromAccount.setBalance(discountBalance);
        toAccount.setBalance(additionBalance);

        LOG.log(Level.INFO, "From: {0}", fromAccount);
        LOG.log(Level.INFO, "To: {0}", toAccount);

        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDateTimeTransaction(LocalDateTime.now());

        if (repository.save(transaction) == null) {
            throw new RuntimeException(transaction + " not saved");
        }

        LOG.log(Level.INFO, "{0} saved", transaction);
        return transaction;
    }

    private void validateTransaction(TransactionRequest transactionRequest) {
        if (transactionRequest == null) {
            throw new EntityNotValidException("Request cannot be null");
        }
        if (transactionRequest.getFromAccount() == null) {
            throw new EntityNotValidException("An origin account number is required");
        }
        if (transactionRequest.getToAccount() == null) {
            throw new EntityNotValidException("A destination account number is required");
        }
        if (transactionRequest.getFromAccount().equals(transactionRequest.getToAccount())) {
            throw new TransactionNotValidException("The origin and destination account numbers are the same");
        }
        if (transactionRequest.getAmount() <= 0) {
            throw new TransactionNotValidException("Transactions with amount equal or less than 0 are not permitted");
        }
    }

    private boolean exists(Account account) {
        return repository
                .findById(account.getNumber())
                .isPresent();
    }

    public List<Transaction> findByDate(LocalDate localDate) {
        List<Transaction> transactions = repository.findByDate(localDate);
        if (transactions.size() > 0) {
            LOG.log(Level.INFO, "Transactions from " + localDate + " were found: {0}", transactions);
            return transactions;
        }
        throw new TransactionNotFoundException("Transactions from " + localDate + " not found");
    }

    public List<Transaction> findByFromAccount(Long fromAccount) {
        List<Transaction> transactions = repository.findByFromAccount(fromAccount);
        if (transactions.size() > 0) {
            LOG.log(Level.INFO, "Transactions were found: {0}", transactions);
            return transactions;
        }
        throw new TransactionNotFoundException("Transaction from account number " + fromAccount + " not found");
    }

    public List<Transaction> findByToAccount(Long toAccount) {
        List<Transaction> transactions = repository.findByToAccount(toAccount);
        if (transactions.size() > 0) {
            LOG.log(Level.INFO, "Transactions were found: {0}", transactions);
            return transactions;
        }
        throw new TransactionNotFoundException("Transaction from account number " + toAccount + " not found");
    }

    public Transaction findById(Long id) throws TransactionNotFoundException {
        Optional<Transaction> transaction = repository.findById(id);

        if (transaction.isPresent()) {
            LOG.log(Level.INFO, "Transaction {0} found", id);
            return transaction.get();
        }

        LOG.log(Level.INFO, "Transaction {0} not found", id);
        throw new TransactionNotFoundException("Transaction with id " + id + " not found");
    }

    public List<Transaction> findAll() {
        List<Transaction> transactions = repository.findAll();
        LOG.log(Level.INFO, "Total of transactions found: {0}", transactions.size());
        return transactions;
    }
}
