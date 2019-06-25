package com.service;

import com.erommel.exception.EntityAlreadyExistsException;
import com.erommel.exception.EntityNotFoundException;
import com.erommel.repository.ClientRepository;
import com.erommel.model.Client;

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

        LOG.log(Level.INFO, "{} saved", client);

    }

    private boolean exists(Client client) {
        return repository
                .findByDocument(client.getDocumentId())
                .isPresent();
    }

    public Client findById(Long id) throws EntityNotFoundException {
        Optional<Client> clientOptional = repository.findById(id);

        if (clientOptional.isPresent()) {
            LOG.log(Level.INFO, "Client {0} found", id);
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