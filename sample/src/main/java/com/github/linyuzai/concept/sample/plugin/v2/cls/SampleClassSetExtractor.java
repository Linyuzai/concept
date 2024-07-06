package com.github.linyuzai.concept.sample.plugin.v2.cls;

import com.github.linyuzai.concept.sample.plugin.CustomPlugin;
import com.github.linyuzai.concept.sample.plugin.v2.PluginConfig;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.jar.handle.extract.ClassExtractor;
import com.github.linyuzai.plugin.jar.handle.extract.match.PluginClassName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class SampleClassSetExtractor extends ClassExtractor<Set<Class<? extends CustomPlugin>>> {

    @Override
    public void onExtract(@PluginClassName(PluginConfig.CLASS_NAME_FILTER) Set<Class<? extends CustomPlugin>> plugins, PluginContext context) {
        log.info("Set => {}", plugins);
    }
}
