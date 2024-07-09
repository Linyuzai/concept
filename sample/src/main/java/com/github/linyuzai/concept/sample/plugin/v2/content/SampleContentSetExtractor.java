package com.github.linyuzai.concept.sample.plugin.v2.content;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.ContentExtractor;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginEntry;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SampleContentSetExtractor extends ContentExtractor<Set<? extends InputStream>> {

    @Override
    public void onExtract(@PluginEntry("**/Text.txt") Set<? extends InputStream> plugins, PluginContext context) {
        log.info("Content Set => {}", plugins.stream().map(new Function<InputStream, String>() {
            @SneakyThrows
            @Override
            public String apply(InputStream is) {
                return new String(PluginUtils.read(is));
            }
        }).collect(Collectors.toSet()));
    }
}
