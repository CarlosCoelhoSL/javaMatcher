package com.matcher.matcher.accountDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MockAccountGenerateService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountIdGenerationService accountIdGenerationService;

    public void generateAccounts() {
        System.out.println("hi from generate accounts");
        Account account1=  Account.builder()
                .id(accountIdGenerationService.newAccountId())
                .account("javainuse")
                .password("$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6")
                .build();
        accountRepository.save(account1);

        Account account2=  Account.builder()
                .id(accountIdGenerationService.newAccountId())
                .account("Test2")
                .password("password2")
                .build();
        System.out.println(account1+"\n"+account2);
        accountRepository.save(account2);
    }
}