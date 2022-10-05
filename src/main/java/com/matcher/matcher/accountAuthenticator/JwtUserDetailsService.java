package com.matcher.matcher.accountAuthenticator;

import java.util.ArrayList;
import java.util.List;

import com.matcher.matcher.accountDB.Account;
import com.matcher.matcher.accountDB.AccountQueryService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final AccountQueryService accountQueryService;

    public JwtUserDetailsService(AccountQueryService accountQueryService) {
        this.accountQueryService = accountQueryService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Account> accountList = accountQueryService.accountRepository.findAllByAccount(username);
        if (accountList.size() > 0) {
            return new User(username, accountList.get(0).getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
