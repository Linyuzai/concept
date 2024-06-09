package com.github.linyuzai.plugin.autoconfigure;

import com.github.linyuzai.plugin.autoconfigure.preperties.PluginConceptProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnableConfigurationProperties(PluginConceptProperties.class)
@Import(PluginConceptConfiguration.class)
public @interface EnablePluginConcept {


}
