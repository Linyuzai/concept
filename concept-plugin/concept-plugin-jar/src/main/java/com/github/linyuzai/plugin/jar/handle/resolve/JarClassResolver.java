package com.github.linyuzai.plugin.jar.handle.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.handle.resolve.EntryResolver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 类解析器
 */
@HandlerDependency(EntryResolver.class)
public class JarClassResolver extends AbstractPluginResolver<Plugin.Entry, JarClass> {

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
    public JarClass doResolve(Plugin.Entry entry, PluginContext context) {
        String name = entry.getName();
        String className = name.substring(0, name.lastIndexOf("."))
                .replace("/", ".");
        Plugin plugin = context.getPlugin();
        return new ClassSupplier(className, plugin);
    }

    @Override
    public Object getInboundKey() {
        return Plugin.Entry.class;
    }

    @Override
    public Object getOutboundKey() {
        return JarClass.class;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ClassSupplier implements JarClass {

        private final String name;

        private final Plugin plugin;

        private volatile Class<?> read;

        @Override
        public Class<?> get() {
            if (read == null) {
                synchronized (this) {
                    if (read == null) {
                        this.read = plugin.read(Class.class, name);
                    }
                }
            }
            return read;
        }
    }
}
