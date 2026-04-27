package org.example.writer.service;

import org.example.shared.UrlProperties;
import org.example.shared.model.UrlEntry;
import org.example.shared.repository.UrlRepository;
import org.example.writer.util.Base62Encoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

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

        while (repository.existsById(code)) {
            code = Base62Encoder.generateCode(originalUrl, properties.getCodeLength());
        }

        Duration ttl = Duration.ofSeconds(properties.getTtlSeconds());
        UrlEntry entry = new UrlEntry(code, originalUrl, Instant.now(), ttl);
        repository.save(entry);
        return code;
    }
}
