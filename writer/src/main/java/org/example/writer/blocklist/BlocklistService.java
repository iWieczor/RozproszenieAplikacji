package org.example.writer.blocklist;

import tools.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

@Service
public class BlocklistService {

    private static final Logger log = LoggerFactory.getLogger(BlocklistService.class);

    private final BlocklistProperties properties;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public BlocklistService(BlocklistProperties properties,
                            KafkaTemplate<String, String> kafkaTemplate,
                            ObjectMapper objectMapper) {
        this.properties = properties;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Sprawdza URL pod kątem zakazanych słów. Jeśli znajdzie zakazane słowo,
     * publikuje zdarzenie na Kafkę i zwraca to słowo. W przeciwnym razie zwraca pusty Optional.
     */
    public Optional<String> screen(String url) {
        String lower = url.toLowerCase(Locale.ROOT);
        for (String word : properties.getWords()) {
            if (word == null || word.isBlank()) {
                continue;
            }
            if (lower.contains(word.toLowerCase(Locale.ROOT))) {
                publish(url, word);
                return Optional.of(word);
            }
        }
        return Optional.empty();
    }

    private void publish(String url, String word) {
        BlocklistEvent event = new BlocklistEvent(Instant.now(), url, word);
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(properties.getTopic(), word, payload);
            log.warn("Zablokowano URL zawierający zakazane słowo '{}': {}", word, url);
        } catch (Exception e) {
            log.error("Nie udało się wysłać zdarzenia blocklist na Kafkę", e);
        }
    }
}
