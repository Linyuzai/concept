package com.github.linyuzai.router.autoconfigure.management;

import com.github.linyuzai.router.core.concept.PathPatternRouter;
import com.github.linyuzai.router.core.concept.Router;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 视图转换器实现类
 */
public class DefaultRouterConvertor implements RouterConvertor {

    @Override
    public RouterVO do2vo(Router router) {
        PathPatternRouter ppr = (PathPatternRouter) router;
        RouterVO vo = new RouterVO();
        vo.setId(ppr.getId());
        vo.setServiceId(ppr.getServiceId());
        vo.setPathPattern(ppr.getPathPattern());
        if ("*".equals(ppr.getPort())) {
            vo.setServerAddress(ppr.getHost());
        } else {
            vo.setServerAddress(ppr.getHost() + ":" + ppr.getPort());
        }
        vo.setForced(ppr.isForced());
        vo.setEnabled(ppr.isEnabled());
        return vo;
    }

    @Override
    public Router vo2do(RouterVO vo) {
        PathPatternRouter router = new PathPatternRouter();
        if (StringUtils.hasText(vo.getId())) {
            router.setId(vo.getId());
        } else {
            router.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        router.setServiceId(vo.getServiceId());
        router.setPathPattern(vo.getPathPattern());
        if (vo.getServerAddress().contains(":")) {
            String[] serverAddresses = vo.getServerAddress().split(":");
            router.setHost(serverAddresses[0]);
            router.setPort(serverAddresses.length > 1 ? serverAddresses[1] : "*");
        } else if (vo.getServerAddress().contains("：")) {
            String[] serverAddresses = vo.getServerAddress().split("：");
            router.setHost(serverAddresses[0]);
            router.setPort(serverAddresses.length > 1 ? serverAddresses[1] : "*");
        } else {
            router.setHost(vo.getServerAddress());
            router.setPort("*");
        }
        router.setForced(vo.getForced());
        router.setEnabled(vo.getEnabled());
        router.setTimestamp(System.currentTimeMillis());
        return router;
    }
}
