package com.github.linyuzai.router.autoconfigure.matcher;

import com.github.linyuzai.router.core.matcher.ServiceRequestRouterMatcher;
import org.springframework.util.AntPathMatcher;

/**
 * 基于 {@link AntPathMatcher} 的服务路由匹配器
 */
public class AntPathRequestRouterMatcher extends ServiceRequestRouterMatcher {

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public boolean matchPattern(String path, String pattern) {
        return matcher.match(pattern, path);
    }

    @Override
    public boolean comparePattern(String path, String matchedNew, String matchedBefore) {
        return matcher.getPatternComparator(path).compare(matchedNew, matchedBefore) < 0;
    }
}
