package com.github.linyuzai.router.core.concept;

import lombok.Getter;
import lombok.Setter;

/**
 * 基于路径匹配的路由
 */
@Getter
@Setter
public class PathPatternRouter extends AbstractRouter implements ServiceRequestRouter {

    private String serviceId;

    private String pathPattern;

    private String host;

    private String port;
}
