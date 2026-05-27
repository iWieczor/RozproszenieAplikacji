package org.example.reader.service;

import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import org.example.shared.model.UrlEntry;
import org.example.shared.repository.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class UrlReaderService {

    private static final Logger log = LoggerFactory.getLogger(UrlReaderService.class);

    private final UrlRepository repository;
    private final CassandraOperations cassandraOperations;

    public UrlReaderService(UrlRepository repository, CassandraOperations cassandraOperations) {
        this.repository = repository;
        this.cassandraOperations = cassandraOperations;
    }

    public Optional<String> resolve(String shortCode) {
        return repository.findById(shortCode)
                .filter(entry -> !entry.isExpired())
                .map(entry -> {
                    touchLastUsed(shortCode);
                    return entry.getOriginalUrl();
                });
    }

    public Optional<UrlEntry> getEntry(String shortCode) {
        return repository.findById(shortCode);
    }

    private void touchLastUsed(String shortCode) {
        Instant now = Instant.now();
        // raw CQL UPDATE — nie nadpisuje natywnego TTL Cassandry
        cassandraOperations.getCqlOperations().execute(
            SimpleStatement.newInstance(
                "UPDATE url_entries SET last_used_at = ? WHERE shortcode = ?",
                now, shortCode
            )
        );
        log.debug("Updated last_used_at for shortCode={} to {}", shortCode, now);
    }
}
