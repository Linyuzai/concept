package com.github.linyuzai.concept.sample.plugin.v2;

import com.github.linyuzai.plugin.autoconfigure.EnablePluginConcept;
import org.springframework.context.annotation.Configuration;

@EnablePluginConcept
@Configuration
public class PluginConfig {

    public static final String CLASS_NAME_FILTER = "com.example.jarplugin.sample.SampleCustomPlugin";

    public static final String CLASS_NAME_SAMPLE_FILTER = CLASS_NAME_FILTER;
}
