package com.github.linyuzai.router.core.matcher;

import com.github.linyuzai.router.core.concept.RequestRouterSource;
import com.github.linyuzai.router.core.concept.Router;
import com.github.linyuzai.router.core.concept.ServiceRequestRouter;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基于服务请求（路径匹配）的路由匹配器
 */
public abstract class ServiceRequestRouterMatcher extends AbstractRouterMatcher<RequestRouterSource, ServiceRequestRouter> {

    @Override
    public Router doMatch(RequestRouterSource request, Collection<? extends ServiceRequestRouter> routers) {
        //如果路径直接匹配上了就直接返回
        Map<String, ServiceRequestRouter> map = routers.stream()
                .filter(it -> matchServiceId(request, it))
                .collect(Collectors.toMap(ServiceRequestRouter::getPathPattern, Function.identity()));
        String path = request.getUri().getPath();
        ServiceRequestRouter matched = map.get(path);
        if (matched != null && matched.isEnabled()) {
            return matched;
        }

        //通过路径匹配模式来匹配
        for (ServiceRequestRouter router : routers) {
            String pathPattern = router.getPathPattern();
            //路由启用并且能匹配上
            if (router.isEnabled() && matchPattern(path, pathPattern)) {
                if (matched == null) {
                    matched = router;
                } else {
                    //如果之前有匹配上的路由
                    //将两个路由匹配模式进行比较，使用更精确的路由
                    if (comparePattern(path, router.getPathPattern(), matched.getPathPattern())) {
                        matched = router;
                    }
                }
            }
        }
        return matched;
    }

    /**
     * 等于 '*' 或是全等（忽略大小写）
     *
     * @param request 请求
     * @param router  路由
     * @return 服务是否匹配
     */
    public boolean matchServiceId(RequestRouterSource request, ServiceRequestRouter router) {
        return "*".equals(router.getServiceId()) ||
                request.getServiceId().equalsIgnoreCase(router.getServiceId());
    }

    /**
     * 路径模式是否匹配
     *
     * @param path    路径
     * @param pattern 路径匹配模式
     * @return 是否匹配
     */
    public abstract boolean matchPattern(String path, String pattern);

    /**
     * 比较两个路径匹配模式
     *
     * @param path          路径
     * @param matchedNew    新的匹配模式
     * @param matchedBefore 之前的匹配模式
     * @return 如果新的匹配模式更精确则返回 true 否则返回 false
     */
    public abstract boolean comparePattern(String path, String matchedNew, String matchedBefore);

}
