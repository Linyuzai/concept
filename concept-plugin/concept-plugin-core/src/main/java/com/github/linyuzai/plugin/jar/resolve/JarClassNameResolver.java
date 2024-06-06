package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.concept.PluginEntry;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolve.EntryResolver;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;

/**
 * 类名解析器
 */
@HandlerDependency(EntryResolver.class)
public class JarClassNameResolver extends AbstractPluginResolver<PluginEntry, String> {

    @Override
    public boolean doFilter(PluginEntry source, PluginContext context) {
        return source.getName().endsWith(".class");
    }

    /**
     * 将所有 .class 结尾的文件过滤出来，
     * 将路径处理成类名
     *
     * @param context 上下文 {@link PluginContext}
     * @return 类名
     */
    @Override
    public String doResolve(PluginEntry entry, PluginContext context) {
        String name = entry.getName();
        return name.substring(0, name.lastIndexOf("."))
                .replaceAll("/", ".");
    }

    @Override
    public Object getInboundKey() {
        return PluginEntry.class;
    }

    @Override
    public Object getOutboundKey() {
        return JarPlugin.CLASS_NAME;
    }
}
