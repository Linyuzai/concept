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
 * 名称过滤器
 */
@Getter
@FilterWithResolver(PathNamePluginResolver.class)
public class NameFilter extends AbstractPluginFilter<List<String>> {

    /**
     * 名称模式
     */
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

    /**
     * 截取名称，和所有的名称模式匹配
     *
     * @param name 全路径名称
     * @return 如果匹配则返回 true，否则返回 false
     */
    public boolean matchName(String name) {
        for (String n : names) {
            int lastIndexOf = name.lastIndexOf("/");
            if (matcher.match(n, lastIndexOf == -1 ? name : name.substring(lastIndexOf + 1))) {
                return true;
            }
        }
        return false;
    }
}
