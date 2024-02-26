package com.github.linyuzai.concept.sample.connection.loadbalance.sse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${download.password}")
public class SseController {

    @GetMapping
    public void a() {
        System.out.println(1);
    }
}
