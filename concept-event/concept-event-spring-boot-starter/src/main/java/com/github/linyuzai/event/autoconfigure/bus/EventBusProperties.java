package com.github.linyuzai.event.autoconfigure.bus;

import com.github.linyuzai.event.core.config.AbstractPropertiesConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "concept.event.bus")
public class EventBusProperties extends AbstractPropertiesConfig {

    private boolean enabled;

    private String engine;

    private String endpoint;
}
