package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.matcher.AbstractPluginMatcher;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolver.JarClassPluginResolver;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@DependOnResolvers(JarClassPluginResolver.class)
public abstract class ClassMatcher<T> extends AbstractPluginMatcher<Collection<Class<? extends T>>> {

    private boolean equals;

    @Override
    public boolean ifMatch(PluginContext context) {
        Collection<Class<?>> classes = context.get(JarPlugin.CLASSES);
        List<Class<?>> matchedClasses = classes.stream()
                .filter(this::matchClass)
                .collect(Collectors.toList());
        if (matchedClasses.isEmpty()) {
            return false;
        }
        context.set(this, matchedClasses);
        return true;
    }

    public boolean matchClass(Class<?> clazz) {
        Class<?> matchingClass = getMatchingClass();
        if (equals) {
            return matchingClass == clazz;
        } else {
            return matchingClass.isAssignableFrom(clazz);
        }
    }

    @Override
    public Collection<Class<? extends T>> getMatchedPlugin(PluginContext context) {
        return context.get(this);
    }

    public Class<?> getMatchingClass() {
        return null;
    }

    @Override
    public abstract void onMatched(Collection<Class<? extends T>> plugins);
}
