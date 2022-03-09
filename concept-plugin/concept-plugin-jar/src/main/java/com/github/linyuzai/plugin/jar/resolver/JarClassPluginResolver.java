package com.github.linyuzai.plugin.jar.resolver;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolver.PluginResolverChain;
import com.github.linyuzai.plugin.core.resolver.annotation.DependOnResolver;
import com.github.linyuzai.plugin.jar.JarPlugin;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@DependOnResolver(JarClassNamePluginResolver.class)
public class JarClassPluginResolver extends AbstractPluginResolver {

    @Override
    public void resolve(PluginContext context, PluginResolverChain chain) {
        JarPlugin plugin = context.getPlugin();
        ClassLoader classLoader = plugin.getClassLoader();
        Collection<String> classNames = context.get(JarPlugin.CLASS_NAMES);
        List<Class<?>> classes = classNames.stream()
                .map(new Function<String, Class<?>>() {

                    @SneakyThrows
                    @Override
                    public Class<?> apply(String s) {
                        return classLoader == null ? Class.forName(s) : classLoader.loadClass(s);
                    }
                })
                .collect(Collectors.toList());
        context.set(JarPlugin.CLASSES, classes);
        chain.next(context);
    }

    @Override
    public boolean support(PluginContext context) {
        return context.contains(JarPlugin.FILE_NAMES);
    }
}
