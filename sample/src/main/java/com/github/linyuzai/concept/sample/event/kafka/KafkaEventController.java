package com.github.linyuzai.concept.sample.event.kafka;

import com.github.linyuzai.event.core.concept.EventConcept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concept-event")
public class KafkaEventController {

    @Autowired
    private EventConcept concept;

    @GetMapping("/kafka")
    public void send() {
        concept.event("123").publish();
    }
}
