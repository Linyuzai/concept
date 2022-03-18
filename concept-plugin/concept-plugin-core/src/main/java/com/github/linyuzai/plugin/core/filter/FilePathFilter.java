package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.resolve.FilePathNamePluginResolver;
import com.github.linyuzai.plugin.core.util.AntPathMatcher;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@FilterWithResolver(FilePathNamePluginResolver.class)
public class FilePathFilter extends AbstractPluginFilter<List<String>> {

    private final Collection<String> paths;

    private final AntPathMatcher matcher = new AntPathMatcher();

    public FilePathFilter(String... paths) {
        this(Arrays.asList(paths));
    }

    public FilePathFilter(Collection<String> paths) {
        this.paths = paths;
    }

    @Override
    public List<String> doFilter(List<String> plugins) {
        return plugins.stream()
                .filter(it -> filterWithNegation(matchPath(it)))
                .collect(Collectors.toList());
    }

    @Override
    public Object getKey() {
        return Plugin.FILE_NAMES;
    }

    private boolean matchPath(String path) {
        for (String p : paths) {
            if (!p.endsWith("/")) {
                p = p + "/";
            }
            p += "**";
            if (matcher.match(p, path)) {
                return true;
            }
        }
        return false;
    }
}
