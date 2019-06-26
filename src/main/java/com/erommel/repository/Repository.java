package com.erommel.repository;

import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Status;
import javax.transaction.Transaction;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Repository<T, ID extends Serializable> {

    private static final Logger LOG = Logger.getLogger(Repository.class.getName());
    private Class<T> clazz;

    protected Repository() {
        clazz = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public boolean update(T element) {
        Session session = getSession();
        try {
            session.beginTransaction();
            session.update(element);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.log(Level.WARNING, "Error on update " + element.toString(), e);
        } finally {
            session.close();
        }
        return false;
    }

    public Serializable save(T element) {
        Session session = getSession();
        try {

            session.beginTransaction();
            Serializable id = session.save(element);
            session.getTransaction().commit();
            return id;

        } catch (Exception e) {
            session.getTransaction().rollback();
            LOG.log(Level.WARNING, "Error on save " + element.toString(), e);
        } finally {
            session.close();
        }
        return null;
    }

    public boolean remove(T element) {
        Session session = getSession();
        try {

            session.beginTransaction();
            session.remove(element);
            session.getTransaction().commit();
            return true;

        } catch (Exception e) {
            session.getTransaction().rollback();
            throw new RuntimeException("Error on remove " + element.toString(), e);
        } finally {
            session.close();
        }
    }
    public List<T> findAll() {
        try(Session session = getSession()) {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<T> query = criteriaBuilder.createQuery(clazz);
            Root<T> from = query.from(clazz);
            query.select(from);
            return session.createQuery(query).getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error on fetch all " + clazz.getSimpleName());
        }
    }
    public Optional<T> findById(ID id) {
        Session session = getSession();
        try {

            return Optional.of(session.find(clazz, id));

        } catch (Exception e) {
            LOG.log(Level.WARNING, "Error on fetch " + clazz.getSimpleName() + " with id " + id);
        }
        return Optional.empty();
    }

    protected Session getSession() {
        return HibernateConfiguration.getSession();
    }

}