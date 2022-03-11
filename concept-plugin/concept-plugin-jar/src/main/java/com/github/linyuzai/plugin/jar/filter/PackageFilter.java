package com.github.linyuzai.plugin.jar.filter;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.filter.FilterWithResolver;
import com.github.linyuzai.plugin.core.filter.PluginFilter;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolver.JarClassNamePluginResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@FilterWithResolver(JarClassNamePluginResolver.class)
public class PackageFilter implements PluginFilter {

    private final Collection<String> packages;

    public PackageFilter(String... packages) {
        this(Arrays.asList(packages));
    }

    @Override
    public void filter(PluginContext context) {
        Collection<String> classNames = context.get(JarPlugin.CLASS_NAMES);
        List<String> filteredClassNames = classNames.stream()
                .filter(this::inPackages)
                .collect(Collectors.toList());
        context.set(JarPlugin.CLASS_NAMES, filteredClassNames);
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
