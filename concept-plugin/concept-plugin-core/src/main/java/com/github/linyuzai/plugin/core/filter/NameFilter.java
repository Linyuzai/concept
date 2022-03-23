package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.util.AntPathMatcher;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class NameFilter extends AbstractPluginFilter<List<String>> {

    private final Collection<String> names;

    private final AntPathMatcher matcher = new AntPathMatcher();

    public NameFilter(String... names) {
        this(Arrays.asList(names));
    }

    public NameFilter(Collection<String> names) {
        this.names = names;
    }

    @Override
    public Object getKey() {
        return Plugin.PATH_NAME;
    }

    @Override
    public List<String> doFilter(List<String> plugins) {
        return plugins.stream()
                .filter(it -> filterWithNegation(matchName(it)))
                .collect(Collectors.toList());
    }

    public boolean matchName(String name) {
        for (String n : names) {
            int lastIndexOf = name.lastIndexOf("/");
            if (matcher.match(n, name.substring(lastIndexOf + 1))) {
                return true;
            }
        }
        return false;
    }
}
