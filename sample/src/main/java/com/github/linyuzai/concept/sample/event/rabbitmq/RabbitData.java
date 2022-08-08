package com.github.linyuzai.concept.sample.event.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RabbitData {

    private Long id;

    private String name;
}
