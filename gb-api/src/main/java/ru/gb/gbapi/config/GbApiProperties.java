package ru.gb.gbapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "gb.api")
public class GbApiProperties {

    private Connection connection;
    private Endpoint endpoint;

    @Setter
    @Getter
    public static class Connection {
        private Long connectTimeout;
        private Long readTimeout;
        private Long period;
        private Long maxPeriod;
        private Integer maxAttempts;
    }

    @Getter
    @Setter
    public static class Endpoint {
        private String manufacturerUrl;
        private String categoryUrl;
        private String productUrl;
    }
}
