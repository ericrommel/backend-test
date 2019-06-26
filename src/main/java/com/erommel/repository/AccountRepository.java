package com.erommel.repository;

import com.erommel.model.Account;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountRepository extends Repository<Account, Long> {
    private static final Logger LOG = Logger.getLogger(AccountRepository.class.getName());
}