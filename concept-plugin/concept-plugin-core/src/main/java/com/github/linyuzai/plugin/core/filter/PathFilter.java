package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.resolve.PathNamePluginResolver;
import com.github.linyuzai.plugin.core.util.AntPathMatcher;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 路径过滤器
 */
@Getter
@FilterWithResolver(PathNamePluginResolver.class)
public class PathFilter extends AbstractPluginFilter<List<String>> {

    /**
     * 路径模式
     */
    private final Collection<String> paths;

    private final AntPathMatcher matcher = new AntPathMatcher();

    public PathFilter(String... paths) {
        this(Arrays.asList(paths));
    }

    public PathFilter(Collection<String> paths) {
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
        return Plugin.PATH_NAME;
    }

    /**
     * 截取路径，和所有的路径模式匹配
     *
     * @param path 全路径名称
     * @return 如果匹配则返回 true，否则返回 false
     */
    public boolean matchPath(String path) {
        for (String p : paths) {
            if (p.startsWith("/")) {
                p = p.substring(1);
            }
            if (p.endsWith("/")) {
                p = p.substring(0, p.length() - 1);
            }
            int lastIndexOf = path.lastIndexOf("/");
            if (matcher.match(p, lastIndexOf == -1 ? "" : path.substring(0, lastIndexOf))) {
                return true;
            }
        }
        return false;
    }
}
