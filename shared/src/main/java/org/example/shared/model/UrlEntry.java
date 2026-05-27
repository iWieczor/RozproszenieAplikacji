package org.example.shared.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import java.time.Duration;
import java.time.Instant;

@Table("url_entries")
public class UrlEntry {

    @PrimaryKey
    private String shortCode;

    @Column("original_url")
    private String originalUrl;

    @Column("created_at")
    private Instant createdAt;

    @Column("expires_at")
    private Instant expiresAt;

    @Column("last_used_at")
    private Instant lastUsedAt;

    public UrlEntry() {}

    public UrlEntry(String shortCode, String originalUrl, Instant createdAt, Duration ttl) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
        this.expiresAt = createdAt.plus(ttl);
    }

    public boolean isExpired() {
        return expiresAt == null || Instant.now().isAfter(expiresAt);
    }

    public String getShortCode() { return shortCode; }
    public String getOriginalUrl() { return originalUrl; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getLastUsedAt() { return lastUsedAt; }
    public void setLastUsedAt(Instant lastUsedAt) { this.lastUsedAt = lastUsedAt; }
}
