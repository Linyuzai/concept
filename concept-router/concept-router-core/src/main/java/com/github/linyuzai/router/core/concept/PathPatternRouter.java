package com.github.linyuzai.router.core.concept;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PathPatternRouter extends AbstractRouter implements ServiceRequestRouter {

    private String serviceId;

    private String pathPattern;

    private String host;

    private int port;
}
