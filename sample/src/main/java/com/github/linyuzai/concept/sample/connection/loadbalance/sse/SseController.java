package com.github.linyuzai.concept.sample.connection.loadbalance.sse;

import com.github.linyuzai.connection.loadbalance.sse.concept.SseLoadBalanceConcept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("sse")
public class SseController {

    @Autowired
    private SseLoadBalanceConcept concept;

    @GetMapping
    public void test() {
        concept.send("123");
    }

    @GetMapping("/create")
    public SseEmitter create() {
        return new SseEmitter(0L);
    }
}
