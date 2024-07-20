package com.adityatomar.generate_token_edra.cron;

import com.adityatomar.generate_token_edra.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Cron {
    private final TokenService tokenService;

    @Scheduled(cron = "0 * * * * *")
    public void deleteExpiredTokensCron() {
        tokenService.deleteExpiredTokens();
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void unblockTokensCron() {
        tokenService.unblockTokens();
    }
}
