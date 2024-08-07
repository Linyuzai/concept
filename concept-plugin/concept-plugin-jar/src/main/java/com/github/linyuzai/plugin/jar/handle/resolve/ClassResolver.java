package com.github.linyuzai.plugin.jar.handle.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.handle.resolve.AbstractSupplier;
import com.github.linyuzai.plugin.core.handle.resolve.EntryResolver;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.PluginClassLoader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * 类解析器
 */
@HandlerDependency(EntryResolver.class)
public class ClassResolver extends AbstractPluginResolver<Plugin.Entry, ClassSupplier> {

    /**
     * 过滤 .class 文件
     */
    @Override
    public boolean doFilter(Plugin.Entry source, PluginContext context) {
        return source.getName().endsWith(".class");
    }

    @Override
    public ClassSupplier doResolve(Plugin.Entry entry, PluginContext context) {
        String name = entry.getName();
        String className = name.substring(0, name.lastIndexOf("."))
                .replace("/", ".");
        Plugin plugin = context.getPlugin();
        return new ClassSupplierImpl(className, plugin);
    }

    @Override
    public Object getInboundKey() {
        return Plugin.Entry.class;
    }

    @Override
    public Object getOutboundKey() {
        return ClassSupplier.class;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ClassSupplierImpl extends AbstractSupplier<Class<?>> implements ClassSupplier {

        private final String name;

        private final Plugin plugin;

        @SneakyThrows
        @Override
        public Class<?> create() {
            if (plugin instanceof JarPlugin) {
                PluginClassLoader pluginClassLoader = ((JarPlugin) plugin).getPluginClassLoader();
                if (pluginClassLoader == null) {
                    return null;
                }
                return pluginClassLoader.loadClass(name);
            }
            return null;
        }
    }
}
