package org.example.reader;

import org.example.shared.repository.UrlRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class ExpiredUrlCleaner {

    private final UrlRepository repository;

    public ExpiredUrlCleaner(UrlRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedDelayString = "${url-shortener.cleanup-interval-ms:60000}")
    @Transactional
    public void cleanup() {
        repository.removeExpiredBefore(Instant.now());
    }
}
