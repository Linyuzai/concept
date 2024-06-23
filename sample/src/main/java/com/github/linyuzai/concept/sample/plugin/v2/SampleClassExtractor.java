package com.github.linyuzai.concept.sample.plugin.v2;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.jar.handle.extract.ClassExtractor;
import com.github.linyuzai.plugin.jar.handle.extract.match.PluginClassName;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SampleClassExtractor extends ClassExtractor<List<? extends Class<?>>> {

    @Override
    public void onExtract(@PluginClassName("com.example.plugin.**") List<? extends Class<?>> plugins, PluginContext context) {
        System.out.println(plugins);
    }
}
