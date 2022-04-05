package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 实例解析器
 */
@DependOnResolvers(JarClassPluginResolver.class)
public class JarInstancePluginResolver extends AbstractPluginResolver<Map<String, Class<?>>, Map<String, Object>> {

    /**
     * 对所有的类尝试实例化
     *
     * @param classMap 类
     * @param context  上下文 {@link PluginContext}
     * @return 实例
     */
    @Override
    public Map<String, Object> doResolve(Map<String, Class<?>> classMap, PluginContext context) {
        Map<String, Object> instanceMap = new LinkedHashMap<>();
        for (Map.Entry<String, Class<?>> entry : classMap.entrySet()) {
            Class<?> value = entry.getValue();
            if (canNewInstance(value)) {
                instanceMap.put(entry.getKey(), newInstance(value));
            }
        }
        return instanceMap;
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
    public Object getDependedKey() {
        return JarPlugin.CLASS;
    }

    @Override
    public Object getResolvedKey() {
        return JarPlugin.INSTANCE;
    }
}
