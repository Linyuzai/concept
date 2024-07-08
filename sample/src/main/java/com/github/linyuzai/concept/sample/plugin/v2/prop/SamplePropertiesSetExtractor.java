package com.github.linyuzai.concept.sample.plugin.v2.prop;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.PropertiesExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.Set;

@Slf4j
@Component
public class SamplePropertiesSetExtractor extends PropertiesExtractor<Set<Properties>> {

    @Override
    public void onExtract(Set<Properties> plugins, PluginContext context) {
        log.info("Properties Set => {}", plugins);
    }
}
