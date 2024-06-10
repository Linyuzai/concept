package com.github.linyuzai.plugin.jar.filter;

import com.github.linyuzai.plugin.core.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarClassNameResolver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;

/**
 * 类名过滤器
 */
@Getter
@RequiredArgsConstructor
@HandlerDependency(JarClassNameResolver.class)
public class ClassNameFilter extends AbstractPluginFilter<String> {

    /**
     * 类名
     */
    private final Collection<String> classNames;

    public ClassNameFilter(String... classNames) {
        this(Arrays.asList(classNames));
    }

    @Override
    public boolean doFilter(String className) {
        return matchClassName(className);
    }

    @Override
    public Object getKey() {
        return JarPlugin.ClassName.class;
    }

    /**
     * 类名是否匹配
     *
     * @param className 类名
     * @return 如果类名匹配返回 true 否则返回 false
     */
    public boolean matchClassName(String className) {
        for (String name : classNames) {
            if (className.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
