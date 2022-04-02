package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;

import java.util.List;
import java.util.stream.Collectors;

@DependOnResolvers(PathNamePluginResolver.class)
public class PropertiesNamePluginResolver extends AbstractPluginResolver<List<String>, List<String>> {

    @Override
    public List<String> doResolve(List<String> filenames, PluginContext context) {
        return filenames.stream()
                .filter(it -> it.endsWith(".properties"))
                .collect(Collectors.toList());
    }

    @Override
    public Object getKey() {
        return Plugin.PATH_NAME;
    }

    @Override
    public Object getResolveKey() {
        return Plugin.PROPERTIES_NAME;
    }
}
