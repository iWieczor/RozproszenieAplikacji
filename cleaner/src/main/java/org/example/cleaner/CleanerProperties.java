package org.example.cleaner;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "cleaner")
public class CleanerProperties {

    public enum Strategy {
        BY_CREATION,
        BY_LAST_USED
    }

    private Strategy strategy = Strategy.BY_CREATION;
    private Duration ttl = Duration.ofSeconds(30);
    private String cron = "0/30 * * * * *";

    public Strategy getStrategy() { return strategy; }
    public void setStrategy(Strategy strategy) { this.strategy = strategy; }

    public Duration getTtl() { return ttl; }
    public void setTtl(Duration ttl) { this.ttl = ttl; }

    public String getCron() { return cron; }
    public void setCron(String cron) { this.cron = cron; }
}
