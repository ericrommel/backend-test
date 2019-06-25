package com.erommel.repository;

import com.erommel.model.Account;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;
import java.util.logging.Logger;

public class AccountRepository extends Repository<Account, Long> {

    private static final Logger LOG = Logger.getLogger(AccountRepository.class.getName());

    public int updateFromAccount(long number, double amount) {
        try(Session session = getSession()) {
            Query<Account> query = session.createQuery(
                    "UPDATE Account a SET a.balance = a.balance - :amount WHERE a.number = :number",
                    Account.class
            );
            query.setParameter("amount", amount);
            query.setParameter("number", number);

            return query.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            LOG.warning("Error to find transaction by account " + number);
        }

        return 0;
    }

    public int updateToAccount(long number, double amount) {
        try(Session session = getSession()) {
            System.out.println("before session.createQuery");
            Query query = session.createQuery(
                    "UPDATE Account a SET a.balance = a.balance + :amount WHERE a.number = :number"
            );

            query.setParameter("amount", amount);
            query.setParameter("number", number);

            return query.executeUpdate();

        } catch (Exception e) {
            LOG.warning("Error to find transaction by account " + number);
        }

        return 0;
    }
}