package org.example.cleaner;

import org.example.shared.model.UrlEntry;
import org.example.shared.repository.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class CleanerService {

    private static final Logger log = LoggerFactory.getLogger(CleanerService.class);

    private final UrlRepository repository;
    private final CleanerProperties properties;

    public CleanerService(UrlRepository repository, CleanerProperties properties) {
        this.repository = repository;
        this.properties = properties;
    }

    @Scheduled(cron = "${cleaner.cron}")
    public void clean() {
        Instant threshold = Instant.now().minus(properties.getTtl());

        log.info("=== Cleaner START === strategy={} ttl={} threshold={}",
                properties.getStrategy(), properties.getTtl(), threshold);

        List<UrlEntry> all = repository.findAll();
        log.info("Znaleziono {} wpisów w bazie", all.size());

        List<UrlEntry> toDelete = all.stream()
                .filter(e -> shouldDelete(e, threshold))
                .toList();

        log.info("Wpisów do usunięcia: {}", toDelete.size());

        for (UrlEntry entry : toDelete) {
            log.info("Usuwam: shortCode={} createdAt={} lastUsedAt={}",
                    entry.getShortCode(), entry.getCreatedAt(), entry.getLastUsedAt());
            repository.deleteById(entry.getShortCode());
        }

        if (toDelete.isEmpty()) {
            log.info("Brak wpisów do usunięcia.");
        } else {
            log.info("Usunięto {} wpisów.", toDelete.size());
        }

        log.info("=== Cleaner END ===");
    }

    private boolean shouldDelete(UrlEntry entry, Instant threshold) {
        // ghost row (createdAt=null) — pozostałość po wygaśnięciu natywnego TTL Cassandry, zawsze usuwamy
        if (entry.getCreatedAt() == null) {
            return true;
        }
        return switch (properties.getStrategy()) {
            case BY_CREATION -> entry.getCreatedAt().isBefore(threshold);
            case BY_LAST_USED -> {
                if (entry.getLastUsedAt() != null) {
                    yield entry.getLastUsedAt().isBefore(threshold);
                }
                // wpis nigdy nieużywany — oceniamy po createdAt
                yield entry.getCreatedAt().isBefore(threshold);
            }
        };
    }
}
