package com.github.linyuzai.concept.sample.plugin.v2;

import com.github.linyuzai.plugin.autoconfigure.EnablePluginConcept;
import com.github.linyuzai.plugin.core.handle.filter.EntryFilter;
import com.github.linyuzai.plugin.core.handle.filter.PluginFilter;
import com.github.linyuzai.plugin.jar.handle.filter.ClassFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Modifier;

@EnablePluginConcept
@Configuration
public class PluginConfig {

    public static final String CLASS_NAME_FILTER = "com.example.jarplugin.sample.SampleCustomPlugin";

    public static final String CLASS_NAME_SAMPLE_FILTER = CLASS_NAME_FILTER;

    //@Bean
    public PluginFilter pluginFilter() {
        ClassFilter.modifier(Modifier::isFinal);
        return new EntryFilter("**/**.properties");
    }
}
