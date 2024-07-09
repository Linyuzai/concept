package com.github.linyuzai.concept.sample.plugin.v2.content;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.ContentExtractor;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SampleContentExtractor extends ContentExtractor<String> {

    @PluginEntry("content/**")
    @Override
    public void onExtract(String plugin, PluginContext context) {
        log.info("Content Object => {}", plugin);
    }
}
