package org.example.writer.blocklist;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "blocklist")
public class BlocklistProperties {

    /** Lista zakazanych słów szukanych w URL-u (porównanie bez uwzględniania wielkości liter). */
    private List<String> words = new ArrayList<>();

    /** Nazwa topicu Kafki, na który trafiają zdarzenia o zablokowanych URL-ach. */
    private String topic = "url-blocklist-events";

    public List<String> getWords() { return words; }
    public void setWords(List<String> words) { this.words = words; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
}
