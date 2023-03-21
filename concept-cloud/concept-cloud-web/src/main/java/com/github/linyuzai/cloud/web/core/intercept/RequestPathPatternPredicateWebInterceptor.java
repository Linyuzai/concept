package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.Collection;

/**
 * 匹配请求路径的断言拦截器
 */
public class RequestPathPatternPredicateWebInterceptor extends PredicateWebInterceptor {

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Getter
    @Setter
    private Collection<String> patterns;

    public RequestPathPatternPredicateWebInterceptor(String... paths) {
        this(Arrays.asList(paths));
    }

    public RequestPathPatternPredicateWebInterceptor(Collection<String> paths) {
        setPatterns(paths);
        setPredicate(this::match);
    }

    @Override
    public RequestPathPatternPredicateWebInterceptor negate() {
        super.negate();
        return this;
    }

    /**
     * 是否匹配路径
     *
     * @param context 上下文
     * @return true 匹配，false 不匹配
     */
    protected boolean match(WebContext context) {
        String path = context.get(WebContext.Request.PATH);
        for (String pattern : patterns) {
            if (matcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }
}
