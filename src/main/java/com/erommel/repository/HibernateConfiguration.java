package com.erommel.repository;

import com.erommel.model.Account;
import com.erommel.model.Client;
import com.erommel.model.Transaction;
import org.h2.Driver;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.H2Dialect;

public final class HibernateConfiguration {

    private static SessionFactory sessionFactory;

    static {
        Configuration config = new Configuration();
        config.addAnnotatedClass(Client.class)
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(Transaction.class)
                .setProperty("hibernate.dialect", H2Dialect.class.getName())
                .setProperty("hibernate.connection.driver_class", Driver.class.getName())
                .setProperty("hibernate.connection.url", "jdbc:h2:mem:am_db")
                .setProperty("hibernate.connection.username", "sa")
                .setProperty("hibernate.connection.password", "")
                .setProperty("hibernate.connection.pool_size", "10")
                .setProperty("hibernate.hbm2ddl.auto", "create");
        sessionFactory = config.buildSessionFactory();
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

}
