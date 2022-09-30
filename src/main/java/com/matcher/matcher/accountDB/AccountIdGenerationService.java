package com.matcher.matcher.accountDB;

import org.springframework.stereotype.Service;

@Service
public class AccountIdGenerationService {
    public Long newAccountId() {
        return System.nanoTime();
    }
}
