package com.github.linyuzai.plugin.autoconfigure.yaml;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.extract.match.AbstractPluginMatcher;
import lombok.Getter;

import java.lang.annotation.Annotation;

/**
 * {@link org.yaml.snakeyaml.Yaml} 匹配器
 */
@Getter
@HandlerDependency(YamlResolver.class)
public class YamlMatcher extends AbstractPluginMatcher<YamlSupplier> {

    public YamlMatcher(Annotation[] annotations) {
        super(annotations);
    }

    @Override
    public Object getKey() {
        return YamlSupplier.class;
    }

    @Override
    public boolean doFilter(YamlSupplier yamlSupplier, PluginContext context) {
        return true;
    }
}
