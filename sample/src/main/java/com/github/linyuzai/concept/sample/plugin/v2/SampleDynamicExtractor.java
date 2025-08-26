package com.github.linyuzai.concept.sample.plugin.v2;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.handle.extract.OnPluginExtract;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginEntry;
import com.github.linyuzai.plugin.jar.handle.extract.match.PluginClassName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

@Slf4j
//@Component
public class SampleDynamicExtractor {

    @OnPluginExtract
    public void noExtract(Plugin plugin) {
        log.error("No Extract: {}", plugin);
    }

    @OnPluginExtract
    public void sampleExtract(List<Object> beans,
                              List<Properties> properties,
                              List<Class<?>> classes,
                              @PluginEntry("content/**") String text,
                              Plugin plugin) {
        log.info("Dynamic Bean: {}", beans);
        log.info("Dynamic Prop: {}", properties);
        log.info("Dynamic Class: {}", classes);
        log.info("Dynamic Text: {}", text);
        SpringData bind = plugin.getMetadata().bind("spring-sample", SpringData.class);
        log.info("Dynamic Bind: {}", bind);
        /*plugin.addLoadListener(new Plugin.LoadListener() {
            @Override
            public void onLoad(Plugin plugin) {
                log.info("Dynamic Load");
            }
        });*/
        plugin.addDestroyListener(new Plugin.DestroyListener() {
            @Override
            public void onDestroy(Plugin plugin) {
                log.info("Dynamic Destroy");
            }
        });
    }

    @Data
    public static class SpringData {

        private String name;

        private String value;
    }
}
