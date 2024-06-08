package com.github.linyuzai.plugin.jar.filter;

import com.github.linyuzai.plugin.core.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.util.AntPathMatcher;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarClassNameResolver;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;

/**
 * 包名过滤器
 */
@Getter
@HandlerDependency(JarClassNameResolver.class)
public class PackageFilter extends AbstractPluginFilter<String> {

    /**
     * 包名
     */
    private final Collection<String> packages;

    private final AntPathMatcher matcher = new AntPathMatcher(".");

    public PackageFilter(String... packages) {
        this(Arrays.asList(packages));
    }

    public PackageFilter(Collection<String> packages) {
        this.packages = packages;
    }

    @Override
    public boolean doFilter(String packageName) {
        return matchPackage(packageName);
    }

    @Override
    public Object getKey() {
        return JarPlugin.CLASSNAME;
    }

    /**
     * 类的包名是否匹配
     *
     * @param className 类名
     * @return 如果包名匹配返回 true 否则返回 false
     */
    public boolean matchPackage(String className) {
        for (String p : packages) {
            int lastIndexOf = className.lastIndexOf(".");
            if (lastIndexOf == -1) {
                return false;
            }
            if (matcher.match(p, className.substring(0, lastIndexOf))) {
                return true;
            }
        }
        return false;
    }
}
