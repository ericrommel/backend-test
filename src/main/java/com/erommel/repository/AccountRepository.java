package com.erommel.repository;

import com.erommel.model.Account;

import java.util.logging.Logger;

public class AccountRepository extends Repository<Account, Long> {
    private static final Logger LOG = Logger.getLogger(AccountRepository.class.getName());
}