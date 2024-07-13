package com.github.linyuzai.concept.sample.plugin.v2.content;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.ContentExtractor;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginEntry;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Slf4j
@Component
public class SampleContentExtractor extends ContentExtractor<InputStream> {

    @SneakyThrows
    @PluginEntry("content/**")
    @Override
    public void onExtract(InputStream plugin, PluginContext context) {
        log.info("Content Object => {}", new String(PluginUtils.read(plugin)));
    }
}
