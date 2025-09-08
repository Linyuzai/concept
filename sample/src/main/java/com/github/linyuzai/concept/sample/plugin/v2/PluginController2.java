package com.github.linyuzai.concept.sample.plugin.v2;

import com.github.linyuzai.concept.sample.plugin.CustomPlugin;
import com.github.linyuzai.plugin.autoconfigure.observable.PluginObservable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/plugin2")
public class PluginController2 {

    private final AtomicInteger integer = new AtomicInteger();

    @Autowired
    private PluginObservable<String, CustomPlugin> pluginObservable;

    @GetMapping("token")
    public String getToken(@RequestHeader(value = "Authorization", required = false) String header) {
        System.out.println(header);
        return "PluginToken" + integer.getAndIncrement();
    }

    @GetMapping("test")
    public void test() {
        System.out.println(pluginObservable);
    }
}
