package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginObjectMatcher;

/**
 * 插件实例提取器
 */
public abstract class PluginObjectExtractor<T extends Plugin> extends SubClassPluginExtractor<T> {

    @Override
    protected Class<?> getSuperClass() {
        return Plugin.class;
    }

    @Override
    protected PluginMatcher getMatcher(Class<?> cls) {
        return new PluginObjectMatcher(cls);
    }

    /**
     * 插件实例提取执行器工厂
     */
    public static class InvokerFactory extends AbstractPluginExtractor.InvokerFactory {

        @Override
        protected AbstractPluginExtractor<?> createExtractor() {
            return new PluginObjectExtractor<Plugin>() {
                @Override
                public void onExtract(Plugin plugin, PluginContext context) {
                }
            };
        }
    }
}
