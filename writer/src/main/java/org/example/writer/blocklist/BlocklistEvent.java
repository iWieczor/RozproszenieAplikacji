package org.example.writer.blocklist;

import java.time.Instant;

/** Zdarzenie wysyłane na Kafkę, gdy ktoś wrzuci URL zawierający zakazane słowo. */
public record BlocklistEvent(Instant detectedAt, String url, String forbiddenWord) {
}
