package com.matcher.matcher.accountDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountQueryService {

    @Autowired
    public AccountRepository accountRepository;

    public Optional<Account> getAccountById(Long accountId) {
        return  accountRepository.findById(accountId);
    }


    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }
}
