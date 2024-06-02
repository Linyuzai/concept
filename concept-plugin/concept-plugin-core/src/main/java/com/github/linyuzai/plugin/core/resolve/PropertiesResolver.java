package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * {@link Properties} 解析器
 */
@HandlerDependency(PropertiesNameResolver.class)
public abstract class PropertiesResolver extends AbstractPluginResolver<List<String>, Map<String, Properties>> {

    /**
     * 将所有名称对象的文件都加载到 {@link Properties} 对象中
     *
     * @param propertiesNames 名称
     * @param context         上下文 {@link PluginContext}
     * @return {@link Properties} 的 {@link Map}
     */
    @Override
    public Map<String, Properties> doResolve(List<String> propertiesNames, PluginContext context) {
        Map<String, Properties> propertiesMap = new LinkedHashMap<>();
        for (String propertiesName : propertiesNames) {
            propertiesMap.put(propertiesName, load(context, propertiesName));
        }
        return propertiesMap;
    }

    @Override
    public Object getInboundKey() {
        return Plugin.PROPERTIES_NAME;
    }

    @Override
    public Object getOutboundKey() {
        return Plugin.PROPERTIES;
    }

    /**
     * 根据名称加载一个 {@link Properties} 对象
     *
     * @param context        上下文 {@link PluginContext}
     * @param propertiesName 名称
     * @return {@link Properties} 对象
     */
    public abstract Properties load(PluginContext context, String propertiesName);
}
