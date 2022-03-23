package com.github.linyuzai.plugin.jar.filter;

import com.github.linyuzai.plugin.core.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.core.filter.FilterWithResolver;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.resolve.JarClassNamePluginResolver;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@FilterWithResolver(JarClassNamePluginResolver.class)
public class ClassNameFilter extends AbstractPluginFilter<Map<String, String>> {

    private Collection<String> classNames;

    public ClassNameFilter(String... classNames) {
        this(Arrays.asList(classNames));
    }

    @Override
    public Map<String, String> doFilter(Map<String, String> plugins) {
        return plugins.entrySet().stream()
                .filter(it -> filterWithNegation(matchClassName(it.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Object getKey() {
        return JarPlugin.CLASS_NAME;
    }

    public boolean matchClassName(String className) {
        for (String name : classNames) {
            if (className.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
