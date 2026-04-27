package org.example.shared.repository;

import org.example.shared.model.UrlEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface UrlRepository extends JpaRepository<UrlEntry, String> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM UrlEntry e WHERE e.expiresAt < :now")
    void removeExpiredBefore(@Param("now") Instant now);
}
