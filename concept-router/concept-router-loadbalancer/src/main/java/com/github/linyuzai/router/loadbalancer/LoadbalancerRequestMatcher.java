package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.Route;
import com.github.linyuzai.router.core.matcher.AbstractRouteMatcher;
import com.github.linyuzai.router.loadbalancer.RequestRouteSource;
import org.springframework.util.AntPathMatcher;

public class LoadbalancerRequestMatcher extends AbstractRouteMatcher {

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public String getServiceId(Route.Source source) {
        RequestRouteSource rrs = (RequestRouteSource) source;
        return rrs.getServiceId();
    }

    @Override
    public String getPath(Route.Source source) {
        RequestRouteSource rrs = (RequestRouteSource) source;
        return rrs.getRequest().getContext().getClientRequest().getUrl().getPath();
    }

    @Override
    public boolean matchPattern(String pattern, String path) {
        return matcher.match(pattern, path);
    }
}
