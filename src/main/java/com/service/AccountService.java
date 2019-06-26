package com.service;

import com.erommel.exception.EntityNotFoundException;
import com.erommel.exception.EntityNotValidException;
import com.erommel.model.Account;
import com.erommel.model.Client;
import com.erommel.repository.AccountRepository;
import com.erommel.rest.request.AccountRequest;

import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountService {

    private static final Logger LOG = Logger.getLogger(AccountService.class.getName());

    private AccountRepository repository;
    private ClientService clientService;

    public AccountService() {
        this.repository = new AccountRepository();
        this.clientService = new ClientService();
    }

    public Account save(AccountRequest accountRequest) throws BadRequestException {
        if (accountRequest == null)
            throw new EntityNotValidException("Request cannot be null");
        if (accountRequest.getClientId() == null || accountRequest.getClientId() <= 0)
            throw new EntityNotValidException("A client id is required");

        Client client = clientService.findById(accountRequest.getClientId());

        Account account = new Account();
        account.setClient(client);
        account.setBalance(accountRequest.getBalance());
        account.setCurrency("HUF");

        if (repository.save(account) == null) {
            throw new RuntimeException("Account " + account.getNumber() + " not saved");
        }

        LOG.log(Level.INFO, "{0} saved", account);
        return account;
    }

    private boolean exists(Account account) {
        return repository
                .findById(account.getNumber())
                .isPresent();
    }

    public Account findByNumber(Long id) {
        Optional<Account> accountOptional = repository.findById(id);

        if (accountOptional.isPresent()) {
            LOG.log(Level.INFO, "Account {0} found", id);
            return accountOptional.get();
        }

        LOG.log(Level.INFO, "Account {0} not found", id);
        throw new EntityNotFoundException("Account with id " + id + " not found");
    }

    public List<Account> findAll() {
        List<Account> accounts = repository.findAll();
        LOG.log(Level.INFO, "Total of accounts found: {0}", accounts.size());
        return accounts;
    }

    public void delete(Long accountNumber) {
        Account account = new Account();
        account.setNumber(accountNumber);

        if (!exists(account))
            throw new EntityNotFoundException("Account " + accountNumber + " not exists");

        repository.remove(account);
    }

    public void updateAccount(Account accountRequest) throws BadRequestException {
        validateAccount(accountRequest);

        if (repository.update(accountRequest)) {
            throw new RuntimeException("fromAccount " + accountRequest.getNumber() + " not update");
        }

        LOG.log(Level.INFO, "{0} updated", accountRequest.getNumber());
    }

    private void validateAccount(Account accountRequest) {
        if (accountRequest == null)
            throw new EntityNotValidException("Request cannot be null");
        if (accountRequest.getNumber() <= 0)
            throw new EntityNotValidException("A valid Account number is required");
    }

}