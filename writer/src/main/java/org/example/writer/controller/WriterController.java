package org.example.writer.controller;

import org.example.shared.UrlProperties;
import org.example.writer.service.UrlShortenerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class WriterController {

    private final UrlShortenerService service;
    private final UrlProperties properties;

    public WriterController(UrlShortenerService service, UrlProperties properties) {
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
}
