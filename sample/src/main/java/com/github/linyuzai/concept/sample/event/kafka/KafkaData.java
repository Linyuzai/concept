package com.github.linyuzai.concept.sample.event.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaData {

    private Long id;

    private String name;
}
