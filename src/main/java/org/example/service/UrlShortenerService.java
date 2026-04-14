package org.example.service;

import org.example.UrlProperties;
import org.example.model.UrlEntry;
import org.example.repository.UrlRepository;
import org.example.util.Base62Encoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class UrlShortenerService {

    private final UrlRepository repository;
    private final UrlProperties properties;

    public UrlShortenerService(UrlRepository repository, UrlProperties properties) {
        this.repository = repository;
        this.properties = properties;
    }
    public String shorten(String originalUrl) {
        String code = Base62Encoder.generateCode(originalUrl, properties.getCodeLength());

        while (repository.findByCode(code).isPresent()) {
            code = Base62Encoder.generateCode(originalUrl, properties.getCodeLength());
        }

        Duration ttl = Duration.ofSeconds(properties.getTtlSeconds());
        UrlEntry entry = new UrlEntry(code, originalUrl, Instant.now(), ttl);
        repository.save(entry);
        return code;
    }

    public Optional<String> resolve(String shortCode) {
        return repository.findByCode(shortCode)
                .filter(entry -> !entry.isExpired())
                .map(UrlEntry::getOriginalUrl);
    }

    public Optional<UrlEntry> getEntry(String shortCode) {
        return repository.findByCode(shortCode);
    }
}
