package com.github.linyuzai.plugin.jar.filter;

import com.github.linyuzai.plugin.core.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.core.filter.FilterWithResolver;
import com.github.linyuzai.plugin.core.util.AntPathMatcher;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarClassNamePluginResolver;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@FilterWithResolver(JarClassNamePluginResolver.class)
public class PackageFilter extends AbstractPluginFilter<Map<String, String>> {

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
                .filter(it -> filterWithNegation(matchPackages(it.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Object getKey() {
        return JarPlugin.CLASS_NAMES;
    }

    private boolean matchPackages(String className) {
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
