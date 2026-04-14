package org.example.model;

import java.time.Duration;
import java.time.Instant;

public class UrlEntry {

    private final String shortCode;
    private final String originalUrl;
    private final Instant createdAt;
    private final Duration ttl;

    public UrlEntry(String shortCode, String originalUrl, Instant createdAt, Duration ttl) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
        this.ttl = ttl;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(createdAt.plus(ttl));
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Duration getTtl() {
        return ttl;
    }

    public Instant getExpiresAt() {
        return createdAt.plus(ttl);
    }
}
