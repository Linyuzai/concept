package com.github.linyuzai.plugin.jar.filter;

import com.github.linyuzai.plugin.core.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.util.AntPathMatcher;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarClassNameResolver;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 包名过滤器
 */
@Getter
@HandlerDependency(JarClassNameResolver.class)
public class PackageFilter extends AbstractPluginFilter<Map<String, String>> {

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
    public Map<String, String> doFilter(Map<String, String> plugins) {
        return plugins.entrySet()
                .stream()
                .filter(it -> filterWithNegation(matchPackage(it.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Object getKey() {
        return JarPlugin.CLASS_NAME;
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
