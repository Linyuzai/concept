package com.github.linyuzai.concept.sample.event;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.template.EventTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class Business1Controller {

    private final EventTemplate template;

    @Autowired
    public Business1Controller(EventConcept concept) {
        this.template = concept.template()
                .exchange(new Business1EventExchange())
                .publisher(new Business1EventPublisher());
    }

    @PostMapping("/business1")
    public void business1() {
        template.publish(new Business1Event());
    }
}
