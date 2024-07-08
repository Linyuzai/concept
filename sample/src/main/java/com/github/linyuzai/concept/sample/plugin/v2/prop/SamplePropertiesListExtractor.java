package com.github.linyuzai.concept.sample.plugin.v2.prop;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.PropertiesExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

@Slf4j
@Component
public class SamplePropertiesListExtractor extends PropertiesExtractor<List<Properties>> {

    @Override
    public void onExtract(List<Properties> plugins, PluginContext context) {
        log.info("Properties List => {}", plugins);
    }
}
