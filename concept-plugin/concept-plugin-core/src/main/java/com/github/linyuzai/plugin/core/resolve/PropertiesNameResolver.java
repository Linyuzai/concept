package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;

import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link java.util.Properties} 名称解析器
 */
@HandlerDependency(PathNameResolver.class)
public class PropertiesNameResolver extends AbstractPluginResolver<List<String>, List<String>> {

    /**
     * 过滤文件名称中 .properties 为后缀的名称
     *
     * @param filenames 文件名称
     * @param context   上下文 {@link PluginContext}
     * @return {@link java.util.Properties} 名称
     */
    @Override
    public List<String> doResolve(List<String> filenames, PluginContext context) {
        return filenames.stream()
                .filter(it -> it.endsWith(".properties"))
                .collect(Collectors.toList());
    }

    @Override
    public Object getInboundKey() {
        return Plugin.PATH_NAME;
    }

    @Override
    public Object getOutboundKey() {
        return Plugin.PROPERTIES_NAME;
    }
}
