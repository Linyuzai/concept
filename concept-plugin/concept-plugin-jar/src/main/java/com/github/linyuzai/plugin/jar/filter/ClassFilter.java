package com.github.linyuzai.plugin.jar.filter;

import com.github.linyuzai.plugin.core.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.core.filter.FilterWithResolver;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarClassPluginResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@FilterWithResolver(JarClassPluginResolver.class)
public class ClassFilter extends AbstractPluginFilter<Map<String, Class<?>>> {

    private Collection<Class<?>> classes;

    public ClassFilter(Class<?>... classes) {
        this(Arrays.asList(classes));
    }

    @Override
    public Map<String, Class<?>> doFilter(Map<String, Class<?>> plugins) {
        return plugins.entrySet().stream()
                .filter(it -> filterWithNegation(matchClasses(it.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Object getKey() {
        return JarPlugin.CLASSES;
    }

    public boolean matchClasses(Class<?> c) {
        for (Class<?> clazz : classes) {
            if (clazz.isAssignableFrom(c)) {
                return true;
            }
        }
        return false;
    }
}
