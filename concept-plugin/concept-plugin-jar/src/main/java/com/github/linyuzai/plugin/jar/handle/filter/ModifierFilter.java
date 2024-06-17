package com.github.linyuzai.plugin.jar.handle.filter;

import com.github.linyuzai.plugin.core.handle.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClassResolver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

/**
 * {@link java.lang.reflect.Modifier} 过滤器
 */
@Getter
@RequiredArgsConstructor
@HandlerDependency(JarClassResolver.class)
public class ModifierFilter extends AbstractPluginFilter<Class<?>> {

    /**
     * {@link java.lang.reflect.Modifier} 对应的 {@link Function}
     */
    private final Collection<Function<Integer, Boolean>> functions;

    @SafeVarargs
    public ModifierFilter(Function<Integer, Boolean>... functions) {
        this(Arrays.asList(functions));
    }

    @Override
    public boolean doFilter(Class<?> clazz) {
        return filterModifier(clazz);
    }

    @Override
    public Object getKey() {
        return Class.class;
    }

    /**
     * 类的 {@link java.lang.reflect.Modifier} 是否匹配
     *
     * @param c 类
     * @return 如果 {@link java.lang.reflect.Modifier} 匹配返回 true 否则返回 false
     */
    private boolean filterModifier(Class<?> c) {
        int modifiers = c.getModifiers();
        for (Function<Integer, Boolean> function : functions) {
            if (function.apply(modifiers)) {
                return true;
            }
        }
        return false;
    }
}
