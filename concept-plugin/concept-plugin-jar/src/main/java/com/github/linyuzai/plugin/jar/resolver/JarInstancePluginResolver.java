package com.github.linyuzai.plugin.jar.resolver;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import lombok.SneakyThrows;

import java.util.List;
import java.util.stream.Collectors;

@DependOnResolvers(JarClassPluginResolver.class)
public class JarInstancePluginResolver extends AbstractPluginResolver {

    @Override
    public void resolve(PluginContext context) {
        List<Class<?>> classes = context.get(JarPlugin.CLASSES);
        List<?> instances = classes.stream()
                .map(this::newInstance)
                .collect(Collectors.toList());
        context.set(JarPlugin.INSTANCES, instances);
    }

    @SneakyThrows
    private Object newInstance(Class<?> clazz) {
        return clazz.newInstance();
    }

    @Override
    public boolean support(PluginContext context) {
        return context.contains(JarPlugin.CLASSES);
    }
}
