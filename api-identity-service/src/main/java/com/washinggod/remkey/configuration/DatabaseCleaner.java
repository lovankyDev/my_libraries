package com.washinggod.remkey.configuration;

import com.washinggod.remkey.repository.InvalidTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DatabaseCleaner {

    @Autowired
    private InvalidTokenRepository invalidTokenRepository;

    @Scheduled(cron = "0 0 0 3 * * *")
    public void cleanupExpiredToken() {
        invalidTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
}
