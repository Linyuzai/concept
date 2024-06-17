package com.github.linyuzai.plugin.jar.handle.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;

/**
 * 类解析器
 */
@HandlerDependency(JarClassNameResolver.class)
public class JarClassResolver extends AbstractPluginResolver<String, Class<?>> {

    /**
     * 对于所有的类名使用类加载器进行加载
     *
     * @param className 类名
     * @param context   上下文 {@link PluginContext}
     * @return 类
     */
    @Override
    public Class<?> doResolve(String className, PluginContext context) {
        Plugin plugin = context.getPlugin();
        return plugin.read(Class.class, className);
    }

    @Override
    public Object getInboundKey() {
        return JarPlugin.ClassName.class;
    }

    @Override
    public Object getOutboundKey() {
        return Class.class;
    }
}
