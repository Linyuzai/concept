package com.github.linyuzai.plugin.jar.handle.filter;

import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClass;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClassResolver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;

/**
 * 类过滤器
 */
@Getter
@RequiredArgsConstructor
@HandlerDependency(JarClassResolver.class)
public class ClassFilter extends AbstractPluginFilter<JarClass> {

    /**
     * 类
     */
    private final Collection<Class<?>> classes;

    public ClassFilter(Class<?>... classes) {
        this(Arrays.asList(classes));
    }

    @Override
    public boolean doFilter(JarClass jarClass) {
        return filterClass(jarClass.get());
    }

    @Override
    public Object getKey() {
        return JarClass.class;
    }

    /**
     * 是否是对应的类或其子类
     *
     * @param cls 类
     * @return 如果是是对应的类或其子类返回 true 否则返回 false
     */
    public boolean filterClass(Class<?> cls) {
        for (Class<?> c : classes) {
            if (c.isAssignableFrom(cls)) {
                return true;
            }
        }
        return false;
    }
}
