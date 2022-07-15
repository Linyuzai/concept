package com.github.linyuzai.event.kafka;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ConfigurationProperties("concept.event")
public class KafkaEventProperties {

    private Map<String, KafkaProperties> kafka = new LinkedHashMap<>();

    @Getter
    @Setter
    public static class KafkaProperties extends org.springframework.boot.autoconfigure.kafka.KafkaProperties {


    }
}
