package com.github.linyuzai.concept.sample.event.kafka;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.kafka.exchange.KafkaEngineExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concept-event")
public class KafkaEventController {

    @Autowired
    private EventConcept concept;

    @Autowired
    @Qualifier("devKafkaTemplate")
    private KafkaTemplate<Object, Object> kafkaTemplate;

    @GetMapping("/kafka")
    public void send() {
        concept.template()
                .exchange(new KafkaEngineExchange())
                .publish(new KafkaData(1L, "kafka"));
    }
}
