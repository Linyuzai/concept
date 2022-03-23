package com.github.linyuzai.plugin.jar.filter;

import com.github.linyuzai.plugin.core.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.core.filter.FilterWithResolver;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarClassPluginResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@FilterWithResolver(JarClassPluginResolver.class)
public class ModifierFilter extends AbstractPluginFilter<Map<String, Class<?>>> {

    private Collection<Function<Integer, Boolean>> functions;

    @SafeVarargs
    public ModifierFilter(Function<Integer, Boolean>... functions) {
        this(Arrays.asList(functions));
    }

    @Override
    public Map<String, Class<?>> doFilter(Map<String, Class<?>> plugins) {
        return plugins.entrySet().stream()
                .filter(it -> filterWithNegation(filterClass(it.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Object getKey() {
        return JarPlugin.CLASS;
    }

    private boolean filterClass(Class<?> c) {
        int modifiers = c.getModifiers();
        for (Function<Integer, Boolean> function : functions) {
            if (function.apply(modifiers)) {
                return true;
            }
        }
        return false;
    }
}
