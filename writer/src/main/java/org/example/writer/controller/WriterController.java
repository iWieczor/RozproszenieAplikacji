package org.example.writer.controller;

import org.example.shared.UrlProperties;
import org.example.writer.blocklist.BlocklistService;
import org.example.writer.service.UrlShortenerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class WriterController {

    private final UrlShortenerService service;
    private final UrlProperties properties;
    private final BlocklistService blocklistService;

    public WriterController(UrlShortenerService service, UrlProperties properties, BlocklistService blocklistService) {
        this.service = service;
        this.properties = properties;
        this.blocklistService = blocklistService;
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<Map<String, String>> shorten(@RequestBody Map<String, String> body) {
        String url = body.get("url");
        if (url == null || url.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Pole 'url' jest wymagane"));
        }

        Optional<String> forbiddenWord = blocklistService.screen(url);
        if (forbiddenWord.isPresent()) {
            return ResponseEntity.unprocessableEntity().body(Map.of(
                    "error", "URL zawiera zakazane słowo i został odrzucony",
                    "forbiddenWord", forbiddenWord.get()
            ));
        }

        String code = service.shorten(url);
        String shortUrl = properties.getBaseUrl() + "/" + code;

        return ResponseEntity.ok(Map.of(
                "shortCode", code,
                "shortUrl", shortUrl
        ));
    }
}
