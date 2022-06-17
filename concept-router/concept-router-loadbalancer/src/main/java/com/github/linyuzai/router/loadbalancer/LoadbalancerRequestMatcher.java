package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.Router;
import com.github.linyuzai.router.core.matcher.AbstractRouterMatcher;
import org.springframework.util.AntPathMatcher;

public class LoadbalancerRequestMatcher extends AbstractRouterMatcher {

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public String getServiceId(Router.Source source) {
        RequestRouterSource rrs = (RequestRouterSource) source;
        return rrs.getServiceId();
    }

    @Override
    public String getPath(Router.Source source) {
        RequestRouterSource rrs = (RequestRouterSource) source;
        return rrs.getRequest().getContext().getClientRequest().getUrl().getPath();
    }

    @Override
    public boolean matchPattern(String pattern, String path) {
        return matcher.match(pattern, path);
    }
}
