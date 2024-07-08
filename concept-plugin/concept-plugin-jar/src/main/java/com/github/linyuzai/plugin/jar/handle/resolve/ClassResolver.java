package com.github.linyuzai.plugin.jar.handle.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.handle.resolve.AbstractSupplier;
import com.github.linyuzai.plugin.core.handle.resolve.EntryResolver;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * 类解析器
 */
@HandlerDependency(EntryResolver.class)
public class ClassResolver extends AbstractPluginResolver<Plugin.Entry, ClassSupplier> {

    @Override
    public boolean doFilter(Plugin.Entry source, PluginContext context) {
        return source.getName().endsWith(".class");
    }

    /**
     * 对于所有的类名使用类加载器进行加载
     *
     * @param entry   类名
     * @param context 上下文 {@link PluginContext}
     * @return 类
     */
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
                return ((JarPlugin) plugin).getPluginClassLoader().loadClass(name);
            }
            return null;
            //return plugin.read(Class.class, name);
        }
    }
}
