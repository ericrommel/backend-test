package com.service;

import com.erommel.exception.EntityNotFoundException;
import com.erommel.exception.EntityNotValidException;
import com.erommel.model.Account;
import com.erommel.model.Transaction;
import com.erommel.repository.TransactionRepository;
import com.erommel.rest.request.AccountRequest;
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
            throw new EntityNotValidException("The origin and destination account numbers are the same");
        }
        if (transactionRequest.getAmount() <= 0) {
            throw new EntityNotValidException("Transactions with amount equal or less than 0 are not permitted");
        }

        Account fromAccount = accountService.findByNumber(transactionRequest.getFromAccount());
        Account toAccount = accountService.findByNumber(transactionRequest.getToAccount());

        Transaction transaction = new Transaction();
        AccountRequest accountRequest = new AccountRequest();

        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        accountRequest.setNumber(transactionRequest.getFromAccount());
        accountRequest.setNumber(transactionRequest.getFromAccount());
        /* Falhando aqui */
        accountService.updateFromAccount(accountRequest);
        accountService.updateToAccount(accountRequest);
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDateTimeTransaction(LocalDateTime.now());

        if (repository.save(transaction) == null) {
            throw new RuntimeException("Transaction " + transaction.getId() + " not saved");
        }

        LOG.log(Level.INFO, "{0} saved", transaction);
        return transaction;
    }

    private boolean exists(Account account) {
        return repository
                .findById(account.getNumber())
                .isPresent();
    }

    public List<Transaction> findByDate(LocalDate localDate) {
        Optional<List<Transaction>> transaction = repository.findByDate(localDate);

        if (transaction.isPresent()) {
            LOG.log(Level.INFO, "Transaction {0} found", localDate);
            return transaction.get();
        }

        LOG.log(Level.INFO, "Transactions {0} not found", localDate);
        throw new EntityNotFoundException("Account with id " + localDate + " not found");
    }

    public Transaction findByFromAccount(Long fromAccount) {
        Optional<Transaction> transaction = repository.findByFromAccount(fromAccount);

        if (transaction.isPresent()) {
            LOG.log(Level.INFO, "Transaction {0} found", fromAccount);
            return transaction.get();
        }

        LOG.log(Level.INFO, "Transaction {0} not found", fromAccount);
        throw new EntityNotFoundException("Transaction with id " + fromAccount + " not found");
    }

    public Transaction findById(Long id) {
        Optional<Transaction> transaction = repository.findById(id);

        if (transaction.isPresent()) {
            LOG.log(Level.INFO, "Transaction {0} found", id);
            return transaction.get();
        }

        LOG.log(Level.INFO, "Transaction {0} not found", id);
        throw new EntityNotFoundException("Transaction with id " + id + " not found");
    }

    public List<Transaction> findAll() {
        List<Transaction> transactionss = repository.findAll();
        LOG.log(Level.INFO, "Total of transactions found: {0}", transactionss.size());
        return transactionss;
    }
}
