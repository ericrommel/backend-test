package com.erommel.repository;

import com.erommel.model.Transaction;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class TransactionRepository extends Repository<Transaction, Long> {
        private static final Logger LOG = Logger.getLogger(TransactionRepository.class.getName());

    public Optional<List<Transaction>> findByDate(LocalDate date) {
        try(Session session = getSession()) {

            Query<Transaction> query = session.createQuery(
                    "SELECT t FROM Transaction t WHERE t.dateTimeTransaction >= :datetime", Transaction.class
            );
            query.setParameter("datetime", date);
            return Optional.of(query.getResultList());
        } catch (Exception e) {
            LOG.warning("Error to find transactions from date " + date);
        }

        return Optional.empty();
    }

    public Optional<Transaction> findByFromAccount(long account) {
        try(Session session = getSession()) {

            Query<Transaction> query = session.createQuery(
                    "SELECT t FROM Transaction t WHERE t.fromAccount = :fromAccount", Transaction.class
            );
            query.setParameter("fromAccount", account);
            return Optional.of(query.getSingleResult());

        } catch (Exception e) {
            LOG.warning("Error to find transaction by account " + account);
        }

        return Optional.empty();
    }

    public Optional<Transaction> findByToAccount(long account) {
        try(Session session = getSession()) {

            Query<Transaction> query = session.createQuery(
                    "SELECT t FROM Transaction t WHERE t.toAccount = :toAccount", Transaction.class
            );
            query.setParameter("toAccount", account);
            return Optional.of(query.getSingleResult());

        } catch (Exception e) {
            LOG.warning("Error to find transaction by account " + account);
        }

        return Optional.empty();
    }
}
