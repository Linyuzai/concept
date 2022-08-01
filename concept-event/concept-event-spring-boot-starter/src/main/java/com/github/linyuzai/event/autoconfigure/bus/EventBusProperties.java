package com.github.linyuzai.event.autoconfigure.bus;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "concept.event.bus")
public class EventBusProperties {

    private boolean enabled;


}
