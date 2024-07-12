package com.github.linyuzai.concept.sample.plugin.v2.content;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.ContentExtractor;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginEntry;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SampleContentSetExtractor extends ContentExtractor<Set<ByteBuffer>> {

    @Override
    public void onExtract(@PluginEntry("**/Text.txt") Set<ByteBuffer> plugins, PluginContext context) {
        log.info("Content Set => {}", plugins.stream().map(it -> new String(it.array()))
                .collect(Collectors.toSet()));
    }
}
