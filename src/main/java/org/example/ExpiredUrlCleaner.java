package org.example;

import org.example.repository.UrlRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExpiredUrlCleaner {

    private final UrlRepository repository;

    public ExpiredUrlCleaner(UrlRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedDelayString = "${url-shortener.cleanup-interval-ms:60000}")
    public void cleanup() {
        repository.removeExpired();
    }
}
