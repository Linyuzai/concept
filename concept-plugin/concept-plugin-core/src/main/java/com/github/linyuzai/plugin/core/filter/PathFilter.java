package com.github.linyuzai.plugin.core.filter;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.resolve.EntryResolver;
import com.github.linyuzai.plugin.core.util.AntPathMatcher;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collection;

/**
 * 路径过滤器
 */
@Getter
@HandlerDependency(EntryResolver.class)
public class PathFilter extends AbstractPluginFilter<Plugin.Entry> {

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
    public boolean doFilter(Plugin.Entry entry) {
        return matchPath(entry.getName());
    }

    @Override
    public Object getKey() {
        return Plugin.Entry.class;
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
