package com.erommel.repository;

import com.erommel.model.Client;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientRepository extends Repository<Client, Long> {

    private static final Logger LOG = Logger.getLogger(ClientRepository.class.getName());

    public Optional<Client> findByDocument(String document) {
        try(Session session = getSession()) {

            Query<Client> query = session.createQuery("SELECT c FROM Client c WHERE c.documentId = :document", Client.class);
            query.setParameter("document", document);
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Error to find client by document " + document, e.getMessage());
        }

        return Optional.empty();
    }
    public Optional<Client> findClientWithAccounts(Long id) {
        try(Session session = getSession()) {

            Query<Client> query = session.createQuery("SELECT c FROM Client c LEFT JOIN FETCH c.accounts " +
                    "WHERE c.id = :id", Client.class);
            query.setParameter("id", id);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            LOG.log(Level.WARNING, "Client id " + id + " there is no account registered", e.getMessage());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error to find client id " + id, e.getMessage());
        }

        return Optional.empty();
    }

    public Optional<Client> findById(Long id) {
        try(Session session = getSession()) {

            Query<Client> query = session.createQuery("SELECT c FROM Client c WHERE c.id = :id", Client.class);
            query.setParameter("id", id);
            return Optional.of(query.getSingleResult());

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error to find client id " + id, e);
        }

        return Optional.empty();
    }
}
