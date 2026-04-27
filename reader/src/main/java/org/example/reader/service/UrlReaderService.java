package org.example.reader.service;

import org.example.shared.model.UrlEntry;
import org.example.shared.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlReaderService {

    private final UrlRepository repository;

    public UrlReaderService(UrlRepository repository) {
        this.repository = repository;
    }

    public Optional<String> resolve(String shortCode) {
        return repository.findById(shortCode)
                .filter(entry -> !entry.isExpired())
                .map(UrlEntry::getOriginalUrl);
    }

    public Optional<UrlEntry> getEntry(String shortCode) {
        return repository.findById(shortCode);
    }
}
