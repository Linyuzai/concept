package com.github.linyuzai.concept.sample.event;

import com.github.linyuzai.event.core.concept.EventConcept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class Business1Controller {

    @Autowired
    private EventConcept concept;

    @PostMapping("/business1")
    public void business1() {
        concept.template()
                .exchange(new Business1EventExchange())
                .publisher(new Business1EventPublisher())
                .publish(new Business1Event());
    }
}
