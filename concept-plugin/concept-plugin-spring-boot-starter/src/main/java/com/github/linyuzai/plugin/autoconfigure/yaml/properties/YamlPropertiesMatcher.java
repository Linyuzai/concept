package com.github.linyuzai.plugin.autoconfigure.yaml.properties;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.extract.match.AbstractPluginMatcher;
import lombok.Getter;

import java.lang.annotation.Annotation;

/**
 * {@link org.yaml.snakeyaml.Yaml} 匹配器
 */
@Getter
@HandlerDependency(YamlPropertiesResolver.class)
public class YamlPropertiesMatcher extends AbstractPluginMatcher<YamlPropertiesSupplier> {

    public YamlPropertiesMatcher(Annotation[] annotations) {
        super(annotations);
    }

    @Override
    public Object getKey() {
        return YamlPropertiesSupplier.class;
    }

    @Override
    public boolean doFilter(YamlPropertiesSupplier yamlPropertiesSupplier, PluginContext context) {
        return true;
    }
}
