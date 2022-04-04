package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import lombok.SneakyThrows;

import java.util.LinkedHashMap;
import java.util.Map;

@DependOnResolvers(JarClassNamePluginResolver.class)
public class JarClassPluginResolver extends AbstractPluginResolver<Map<String, String>, Map<String, Class<?>>> {

    @Override
    public Map<String, Class<?>> doResolve(Map<String, String> classNameMap, PluginContext context) {
        JarPlugin plugin = context.getPlugin();
        ClassLoader classLoader = plugin.getPluginClassLoader();
        Map<String, Class<?>> classMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : classNameMap.entrySet()) {
            classMap.put(entry.getKey(), loadClass(classLoader, entry.getValue()));
        }
        return classMap;
    }

    @SneakyThrows
    private Class<?> loadClass(ClassLoader classLoader, String className) {
        return classLoader.loadClass(className);
    }

    @Override
    public Object getDependedKey() {
        return JarPlugin.CLASS_NAME;
    }

    @Override
    public Object getResolvedKey() {
        return JarPlugin.CLASS;
    }
}
