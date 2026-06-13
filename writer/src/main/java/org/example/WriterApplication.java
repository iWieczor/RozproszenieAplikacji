package org.example;

import org.example.shared.UrlProperties;
import org.example.writer.blocklist.BlocklistProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({UrlProperties.class, BlocklistProperties.class})
public class WriterApplication {

    public static void main(String[] args) {
        SpringApplication.run(WriterApplication.class, args);
    }
}
