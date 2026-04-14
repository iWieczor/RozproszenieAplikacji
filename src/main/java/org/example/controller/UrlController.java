package org.example.controller;

import org.example.UrlProperties;
import org.example.model.UrlEntry;
import org.example.service.UrlShortenerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

/**
 * REST API skracania URL.
 *
 * POST /api/shorten          – tworzy skrócony URL
 * GET  /{code}               – przekierowuje na oryginalny URL
 * GET  /api/info/{code}      – zwraca informacje o wpisie (do celów diagnostycznych)
 */
@RestController
public class UrlController {

    private final UrlShortenerService service;
    private final UrlProperties properties;

    public UrlController(UrlShortenerService service, UrlProperties properties) {
        this.service = service;
        this.properties = properties;
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<Map<String, String>> shorten(@RequestBody Map<String, String> body) {
        String url = body.get("url");
        if (url == null || url.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Pole 'url' jest wymagane"));
        }

        String code = service.shorten(url);
        String shortUrl = properties.getBaseUrl() + "/" + code;

        return ResponseEntity.ok(Map.of(
                "shortCode", code,
                "shortUrl", shortUrl
        ));
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
                .map(entry -> ResponseEntity.ok(Map.<String, Object>of(
                        "shortCode", entry.getShortCode(),
                        "originalUrl", entry.getOriginalUrl(),
                        "createdAt", entry.getCreatedAt().toString(),
                        "expiresAt", entry.getExpiresAt().toString(),
                        "expired", entry.isExpired()
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}
