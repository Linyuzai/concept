package com.github.linyuzai.concept.sample.plugin.v2.content;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.ContentExtractor;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SampleContentListExtractor extends ContentExtractor<List<byte[]>> {

    @Override
    public void onExtract(@PluginEntry("content/**") List<byte[]> plugins, PluginContext context) {
        log.info("Content List => {}", plugins.stream().map(String::new).collect(Collectors.toList()));
    }
}
