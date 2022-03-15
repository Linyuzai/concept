package com.github.linyuzai.plugin.jar.filter;

import com.github.linyuzai.plugin.core.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.core.filter.FilterWithResolver;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolver.JarClassNamePluginResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@FilterWithResolver(JarClassNamePluginResolver.class)
public class PackageFilter extends AbstractPluginFilter<Map<String, String>> {

    private final Collection<String> packages;

    public PackageFilter(String... packages) {
        this(Arrays.asList(packages));
    }

    @Override
    public Map<String, String> doFilter(Map<String, String> plugins) {
        return plugins.entrySet()
                .stream()
                .filter(it -> filterWithNegation(inPackages(it.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Object getKey() {
        return JarPlugin.CLASS_NAMES;
    }

    private boolean inPackages(String className) {
        for (String p : packages) {
            if (className.startsWith(p)) {
                return true;
            }
        }
        return false;
    }
}
