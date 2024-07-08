package com.github.linyuzai.concept.sample.plugin.v2.prop;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.PropertiesExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Slf4j
@Component
public class SamplePropertiesMapExtractor extends PropertiesExtractor<Map<Object, Properties>> {

    @Override
    public void onExtract(Map<Object, Properties> plugins, PluginContext context) {
        log.info("Properties Map => {}", plugins);
    }
}
