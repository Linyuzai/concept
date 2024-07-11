package com.github.linyuzai.concept.sample.plugin.v2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/plugin2")
public class PluginController2 {

    private final AtomicInteger integer = new AtomicInteger();

    @GetMapping("token")
    public String getToken(@RequestHeader(value = "Authorization", required = false) String header) {
        System.out.println(header);
        return "PluginToken" + integer.getAndIncrement();
    }
}
