package com.github.linyuzai.concept.sample.plugin.v2.content;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.ContentExtractor;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SampleContentArrayExtractor extends ContentExtractor<byte[][]> {

    @PluginEntry("**/**.txt")
    @Override
    public void onExtract(byte[][] plugins, PluginContext context) {
        log.info("Content Array => {}", new String(plugins[0]));
    }
}
