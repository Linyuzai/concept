package com.github.linyuzai.concept.sample.plugin.v2.content;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.ContentExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Map;

@Slf4j
@Component
public class SampleContentMapExtractor extends ContentExtractor<Map<Object, ? extends InputStream>> {

    @Override
    public void onExtract(Map<Object, ? extends InputStream> plugins, PluginContext context) {
        log.info("Content Map => {}", plugins);
    }
}
