package com.erommel.repository;

import com.erommel.model.Client;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;
import java.util.logging.Logger;

public class ClientRepository extends Repository<Client, Long> {

    private static final Logger LOG = Logger.getLogger(ClientRepository.class.getName());

    public Optional<Client> findByDocument(String document) {
        try(Session session = getSession()) {

            Query<Client> query = session.createQuery("SELECT c FROM Client c WHERE c.documentId = :document", Client.class);
            query.setParameter("document", document);
            return Optional.of(query.getSingleResult());

        } catch (Exception e) {
            LOG.warning("Error to find client by document " + document);
        }

        return Optional.empty();
    }

}
