package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.util.AntPathMatcher;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FileNameFilter extends AbstractPluginFilter<List<String>> {

    private final Collection<String> names;

    private final AntPathMatcher matcher = new AntPathMatcher();

    public FileNameFilter(String... names) {
        this(Arrays.asList(names));
    }

    public FileNameFilter(Collection<String> names) {
        this.names = names;
    }

    @Override
    public Object getKey() {
        return Plugin.FILE_NAMES;
    }

    @Override
    public List<String> doFilter(List<String> plugins) {
        return plugins.stream()
                .filter(it -> filterWithNegation(matchName(it)))
                .collect(Collectors.toList());
    }

    private boolean matchName(String name) {
        for (String n : names) {
            int lastIndexOf = name.lastIndexOf("/");
            if (matcher.match(n, name.substring(lastIndexOf + 1))) {
                return true;
            }
        }
        return false;
    }
}
