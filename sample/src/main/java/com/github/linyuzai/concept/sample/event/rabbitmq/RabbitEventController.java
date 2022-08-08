package com.github.linyuzai.concept.sample.event.rabbitmq;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.rabbitmq.exchange.RabbitEngineExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concept-event")
public class RabbitEventController {

    @Autowired
    private EventConcept concept;

    @GetMapping("/rabbit")
    public void send() {
        concept.template()
                .exchange(new RabbitEngineExchange())
                .publish(new RabbitData(1L, "rabbit"));
    }
}
