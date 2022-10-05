package com.matcher.matcher.accountDB;

import com.matcher.matcher.accountAuthenticator.JwtRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/account")
public class AccountQueryController {
    @Autowired
    private AccountIdGenerationService accountIdGenerationService;
    @Autowired
    private AccountRepository accountRepository;

    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final AccountQueryService accountQueryService;

    public AccountQueryController(AccountQueryService accountQueryService) {
        this.accountQueryService = accountQueryService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable(value = "accountId") Long accountId) {
        Optional<Account> accountOpt = accountQueryService.getAccountById(accountId);
        return accountOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/list")
    public List<Account> getAccounts() {
        System.out.println("hi from account list");
        return accountQueryService.getAccounts();
    }

    @PostMapping("/newAccount")
    public String addNewAccount(@RequestBody JwtRequest newAccount) {
        System.out.println(newAccount.getUsername()+newAccount.getPassword());
        List<Account> accountList = accountQueryService.accountRepository.findAllByAccount(newAccount.getUsername());
        if (accountList.size() > 0) {
            return "An account already exists with that username";
        } else {
            Account account = Account.builder()
                    .id(accountIdGenerationService.newAccountId())
                    .account(newAccount.getUsername())
                    .password(passwordEncoder().encode(newAccount.getPassword()))
                    .build();
            accountRepository.save(account);
            System.out.println(account);
            return "New account successfully created";
        }

    }

}