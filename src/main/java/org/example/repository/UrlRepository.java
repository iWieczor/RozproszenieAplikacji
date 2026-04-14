package org.example.repository;

import org.example.model.UrlEntry;

import java.util.Optional;


public interface UrlRepository {


    void save(UrlEntry entry);


    Optional<UrlEntry> findByCode(String shortCode);

    void removeExpired();
}
