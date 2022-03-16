package com.github.linyuzai.plugin.jar.resolver;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import lombok.SneakyThrows;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@DependOnResolvers(JarClassNamePluginResolver.class)
public class JarClassPluginResolver extends AbstractPluginResolver {

    @Override
    public void resolve(PluginContext context) {
        JarPlugin plugin = context.getPlugin();
        ClassLoader classLoader = plugin.getClassLoader();
        Map<String, String> classNameMap = context.get(JarPlugin.CLASS_NAMES);
        Map<String, Class<?>> classMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : classNameMap.entrySet()) {
            classMap.put(entry.getKey(), loadClass(classLoader, entry.getValue()));
        }
        context.set(JarPlugin.CLASSES, classMap);
    }

    @SneakyThrows
    private Class<?> loadClass(ClassLoader classLoader, String className) {
        return classLoader.loadClass(className);
    }

    @Override
    public boolean support(PluginContext context) {
        return context.contains(JarPlugin.CLASS_NAMES);
    }
}
