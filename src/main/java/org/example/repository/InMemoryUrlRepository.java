package org.example.repository;

import org.example.model.UrlEntry;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUrlRepository implements UrlRepository {

    private final Map<String, UrlEntry> store = new ConcurrentHashMap<>();

    @Override
    public void save(UrlEntry entry) {
        store.put(entry.getShortCode(), entry);
    }

    @Override
    public Optional<UrlEntry> findByCode(String shortCode) {
        return Optional.ofNullable(store.get(shortCode));
    }

    @Override
    public void removeExpired() {
        store.values().removeIf(UrlEntry::isExpired);
    }
}
