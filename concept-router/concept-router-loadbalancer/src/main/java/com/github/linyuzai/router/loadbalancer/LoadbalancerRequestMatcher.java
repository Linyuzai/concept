package com.github.linyuzai.router.loadbalancer;

import com.github.linyuzai.router.core.concept.AbstractRouter;
import com.github.linyuzai.router.core.concept.Router;
import com.github.linyuzai.router.core.matcher.AbstractRouterMatcher;
import org.springframework.util.AntPathMatcher;

public class LoadbalancerRequestMatcher extends AbstractRouterMatcher {

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public String getServiceId(Router.Source source) {
        AbstractRouter.Source ars = (AbstractRouter.Source) source;
        return ars.getServiceId();
    }

    @Override
    public String getPath(Router.Source source) {
        AbstractRouter.Source ars = (AbstractRouter.Source) source;
        return ars.getUri().getPath();
    }

    @Override
    public boolean matchPattern(String pattern, String path) {
        return matcher.match(pattern, path);
    }
}
