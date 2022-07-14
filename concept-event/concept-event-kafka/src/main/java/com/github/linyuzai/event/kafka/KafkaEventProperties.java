package com.github.linyuzai.event.kafka;

import lombok.Data;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ConfigurationProperties("concept.event")
public class KafkaEventProperties {

    private Map<String, KafkaProperties> kafka = new LinkedHashMap<>();
}
