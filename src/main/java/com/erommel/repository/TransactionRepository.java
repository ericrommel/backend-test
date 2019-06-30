package com.erommel.repository;

import com.erommel.model.Transaction;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionRepository extends Repository<Transaction, Long> {
        private static final Logger LOG = Logger.getLogger(TransactionRepository.class.getName());

    public List<Transaction> findByDate(LocalDate date) {
        try(Session session = getSession()) {
            Query<Transaction> query = session.createQuery(
                    "SELECT t FROM Transaction t WHERE t.dateTimeTransaction >= :datetime", Transaction.class
            );
            query.setParameter("datetime", date.atStartOfDay());
            return query.getResultList();
        } catch (Exception e) {
            LOG.warning("Error to find transactions from date " + date);
            throw new RuntimeException("Error to find transactions from date " + date);
        }
    }

    public List<Transaction> findByFromAccount(Long account) {
        try(Session session = getSession()) {
            Query<Transaction> query = session.createQuery(
                    "SELECT t FROM Transaction t WHERE t.fromAccount.number = :number", Transaction.class
            );
            query.setParameter("number", account);
            return query.getResultList();
        } catch (Exception e) {
            LOG.warning("Error to find transaction by account " + account);
            throw new RuntimeException("Error to find transaction by account " + account);
        }
    }

    public List<Transaction> findByToAccount(long account) {
        try(Session session = getSession()) {
            Query<Transaction> query = session.createQuery(
                    "SELECT t FROM Transaction t WHERE t.toAccount.number = :toAccount", Transaction.class
            );
            query.setParameter("toAccount", account);
            return query.getResultList();

        } catch (Exception e) {
            LOG.warning("Error to find transaction by account " + account);
            throw new RuntimeException("Error to find transaction by account " + account);
        }
    }

    public Optional<Transaction> findById(Long id) {
        try(Session session = getSession()) {

            Query<Transaction> query = session.createQuery("SELECT t FROM Transaction t WHERE t.transaction_id = :id", Transaction.class);
            query.setParameter("id", id);
            return Optional.of(query.getSingleResult());

        } catch (Exception e) {
            LOG.warning("Error to find transaction id " + id);
        }

        return Optional.empty();
    }

    @Override
    public Serializable save(Transaction transaction) {
        Session session = getSession();
        try {
            session.beginTransaction();
            session.update(transaction.getToAccount());
            session.update(transaction.getFromAccount());
            Serializable id = session.save(transaction);
            session.getTransaction().commit();

            return id;

        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.log(Level.WARNING, "Error on save " + transaction.toString(), e);
        } finally {
            session.close();
        }
        return null;
    }

}
