package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.jar.extension.ExJarPlugin;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * 实例解析器
 */
//TODO BeanDefinition
@Deprecated
@HandlerDependency(JarClassResolver.class)
class JarInstanceResolver extends AbstractPluginResolver<Class<?>, Object> {

    @Override
    public boolean doFilter(Class<?> source, PluginContext context) {
        return canNewInstance(source);
    }

    /**
     * 对所有的类尝试实例化
     *
     * @param clazz   类
     * @param context 上下文 {@link PluginContext}
     * @return 实例
     */
    @Override
    public Object doResolve(Class<?> clazz, PluginContext context) {
        return newInstance(clazz);
    }

    /**
     * 是否可以实例化。
     * 如果是接口或是抽象类则不能实例化，
     * 如果不存在 0 个参数的构造器则不能实例化。
     *
     * @param clazz 类
     * @return 如果可以实例化返回 true 否则返回 false
     */
    private boolean canNewInstance(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
            return false;
        }
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过 0 参数的构造器进行实例化
     *
     * @param clazz 类
     * @return 实例
     */
    @SneakyThrows
    private Object newInstance(Class<?> clazz) {
        Constructor<?> constructor = clazz.getConstructor();
        return constructor.newInstance();
    }

    @Override
    public Object getInboundKey() {
        return ExJarPlugin.CLASS;
    }

    @Override
    public Object getOutboundKey() {
        return ExJarPlugin.INSTANCE;
    }
}
