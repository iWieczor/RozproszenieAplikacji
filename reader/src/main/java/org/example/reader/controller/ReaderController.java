package org.example.reader.controller;

import org.example.reader.service.UrlReaderService;
import org.example.shared.model.UrlEntry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
public class ReaderController {

    private final UrlReaderService service;

    public ReaderController(UrlReaderService service) {
        this.service = service;
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        Optional<String> originalUrl = service.resolve(code);

        if (originalUrl.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(originalUrl.get()));
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }

        Optional<UrlEntry> entry = service.getEntry(code);
        if (entry.isPresent() && entry.get().isExpired()) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/api/info/{code}")
    public ResponseEntity<Map<String, Object>> info(@PathVariable String code) {
        return service.getEntry(code)
                .map(entry -> {
                    Map<String, Object> map = new java.util.LinkedHashMap<>();
                    map.put("shortCode", entry.getShortCode());
                    map.put("originalUrl", entry.getOriginalUrl());
                    map.put("createdAt", entry.getCreatedAt() != null ? entry.getCreatedAt().toString() : null);
                    map.put("expiresAt", entry.getExpiresAt() != null ? entry.getExpiresAt().toString() : null);
                    map.put("lastUsedAt", entry.getLastUsedAt() != null ? entry.getLastUsedAt().toString() : null);
                    map.put("expired", entry.isExpired());
                    return ResponseEntity.ok(map);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
