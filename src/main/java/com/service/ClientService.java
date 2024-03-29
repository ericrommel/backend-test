package com.service;

import com.erommel.exception.EntityAlreadyExistsException;
import com.erommel.exception.ClientHasAccountException;
import com.erommel.exception.EntityNotFoundException;
import com.erommel.model.Client;
import com.erommel.repository.ClientRepository;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientService {

    private static final Logger LOG = Logger.getLogger(ClientService.class.getName());

    private ClientRepository repository;

    public ClientService() {
        this.repository = new ClientRepository();
    }

    public void save(Client client) {
        Objects.requireNonNull(client);

        if(exists(client))
            throw new EntityAlreadyExistsException(client + " already exists");

        if (repository.save(client) == null) {
            throw new RuntimeException("Client " + client.getName() + " not saved");
        }

        LOG.log(Level.INFO, "{0} has been saved", client);
    }

    public void update(Client client) {
        Objects.requireNonNull(client);
        if (!exists(client.getId())) {
            LOG.log(Level.INFO, "Client {0} not found", client);
            throw new EntityNotFoundException("Client " + client.getId() + " not found");
        }

        if (repository.update(client)) {
            LOG.log(Level.INFO, "{0} has been updated", client);
        } else {
            LOG.log(Level.INFO, "Client {0} not updated", client);
            throw new RuntimeException("Client " + client.getName() + " not updated");
        }
    }

    public void delete(Long clientId) {
        if (clientId < 1) {
            throw new InvalidParameterException("Invalid value for client id");
        }

        Client client = repository.findClientWithAccounts(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client with id " + clientId + " not found"));

        if (!client.getAccounts().isEmpty()) {
            throw new ClientHasAccountException("Client with id " + clientId + " has one or more accoutns registered.");
        }

        if (!repository.remove(client)) {
            throw new RuntimeException("Client " + client + " not removed");
        }

        LOG.log(Level.INFO, "Client {0} has been removed", clientId);
    }

    private boolean exists(Long id) {
        return repository
                .findById(id)
                .isPresent();
    }

    private boolean exists(Client client) {
        return repository
                .findByDocument(client.getDocumentId())
                .isPresent();
    }

    public Client findById(Long id) throws EntityNotFoundException {
        Optional<Client> clientOptional = repository.findById(id);

        if (clientOptional.isPresent()) {
            LOG.log(Level.INFO, "Client {0} has been found", id);
            return clientOptional.get();
        }

        LOG.log(Level.INFO, "Client {0} not found", id);
        throw new EntityNotFoundException("Client with id " + id + " not found");
    }

    public List<Client> findAll() {
        List<Client> clients = repository.findAll();
        LOG.log(Level.INFO, "Total of clients found: {0}", clients.size());
        return clients;
    }
}