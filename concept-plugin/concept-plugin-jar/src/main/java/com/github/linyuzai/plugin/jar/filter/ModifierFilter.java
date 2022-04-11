package com.github.linyuzai.plugin.jar.filter;

import com.github.linyuzai.plugin.core.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.core.filter.FilterWithResolver;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarClassResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@link java.lang.reflect.Modifier} 过滤器
 */
@Getter
@AllArgsConstructor
@FilterWithResolver(JarClassResolver.class)
public class ModifierFilter extends AbstractPluginFilter<Map<String, Class<?>>> {

    /**
     * {@link java.lang.reflect.Modifier} 对应的 {@link Function}
     */
    private final Collection<Function<Integer, Boolean>> functions;

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

    /**
     * 类的 {@link java.lang.reflect.Modifier} 是否匹配
     *
     * @param c 类
     * @return 如果 {@link java.lang.reflect.Modifier} 匹配返回 true 否则返回 false
     */
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
