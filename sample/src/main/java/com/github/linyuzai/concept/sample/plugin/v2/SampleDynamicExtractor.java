package com.github.linyuzai.concept.sample.plugin.v2;

import com.github.linyuzai.plugin.core.handle.extract.OnPluginExtract;
import com.github.linyuzai.plugin.jar.handle.extract.match.PluginClassName;
import org.springframework.stereotype.Component;

@Component
public class SampleDynamicExtractor {

    @OnPluginExtract
    public void sampleExtract(@PluginClassName("com.example.plugin.**Plugin") Object bean) {
        System.out.println("Dynamic: " + bean);
    }
}
