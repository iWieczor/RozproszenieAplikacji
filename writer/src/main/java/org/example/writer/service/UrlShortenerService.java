package org.example.writer.service;

import org.example.shared.UrlProperties;
import org.example.shared.model.UrlEntry;
import org.example.shared.repository.UrlRepository;
import org.example.writer.util.Base62Encoder;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class UrlShortenerService {

    private final UrlRepository repository;
    private final UrlProperties properties;
    private final CassandraOperations cassandraOperations;

    public UrlShortenerService(UrlRepository repository, UrlProperties properties, CassandraOperations cassandraOperations) {
        this.repository = repository;
        this.properties = properties;
        this.cassandraOperations = cassandraOperations;
    }

    public String shorten(String originalUrl) {
        String code = Base62Encoder.generateCode(originalUrl, properties.getCodeLength());

        while (repository.existsById(code)) {
            code = Base62Encoder.generateCode(originalUrl, properties.getCodeLength());
        }

        Duration ttl = Duration.ofSeconds(properties.getTtlSeconds());
        UrlEntry entry = new UrlEntry(code, originalUrl, Instant.now(), ttl);
        cassandraOperations.insert(entry, InsertOptions.builder().ttl((int) ttl.getSeconds()).build());
        return code;
    }
}
