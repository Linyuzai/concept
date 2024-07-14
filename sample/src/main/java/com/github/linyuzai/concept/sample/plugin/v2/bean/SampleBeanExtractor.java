package com.github.linyuzai.concept.sample.plugin.v2.bean;

import com.github.linyuzai.concept.sample.plugin.CustomPlugin;
import com.github.linyuzai.plugin.autoconfigure.bean.BeanExtractor;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.jar.handle.extract.match.PluginClassName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SampleBeanExtractor extends BeanExtractor<CustomPlugin> {

    @Override
    public void onExtract(@PluginClassName("com.example.jarplugin.inner.**") CustomPlugin plugin, PluginContext context) {
        log.info("Bean Object => {}", plugin);
        try {
            plugin.run();
        } catch (Throwable e) {

        }
    }
}
