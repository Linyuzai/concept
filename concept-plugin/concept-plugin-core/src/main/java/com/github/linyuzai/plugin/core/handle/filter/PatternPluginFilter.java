package com.github.linyuzai.plugin.core.handle.filter;

import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.util.AntPathMatcher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

/**
 * 名称过滤器
 */
@Getter
@RequiredArgsConstructor
public abstract class PatternPluginFilter<T> extends AbstractPluginFilter<T> implements PluginHandler.Property {

    /**
     * 名称模式
     */
    private final Collection<String> patterns;

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public boolean doFilter(T original) {
        return matchPattern(getMatchable(original));
    }

    protected abstract String getMatchable(T original);

    /**
     * 截取名称，和所有的名称模式匹配
     *
     * @param matchable 全路径名称
     * @return 如果匹配则返回 true，否则返回 false
     */
    public boolean matchPattern(String matchable) {
        for (String pattern : patterns) {
            if (matcher.match(pattern, matchable)) {
                return true;
            }
        }
        return false;
    }
}
