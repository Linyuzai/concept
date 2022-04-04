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

@DependOnResolvers(JarClassPluginResolver.class)
public class JarInstancePluginResolver extends AbstractPluginResolver<Map<String, Class<?>>, Map<String, Object>> {

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
