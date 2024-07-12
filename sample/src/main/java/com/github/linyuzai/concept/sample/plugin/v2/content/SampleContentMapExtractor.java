package com.github.linyuzai.concept.sample.plugin.v2.content;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.ContentExtractor;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class SampleContentMapExtractor extends ContentExtractor<Map<Object, String>> {

    @PluginEntry("content/**")
    @Override
    public void onExtract(Map<Object, String> plugins, PluginContext context) {
        log.info("Content Map => {}", plugins);
    }
}
