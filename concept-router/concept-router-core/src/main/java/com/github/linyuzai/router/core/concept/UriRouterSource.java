package com.github.linyuzai.router.core.concept;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URI;

/**
 * 基于 {@link URI} 的路由请求来源
 */
@Getter
@AllArgsConstructor
public class UriRouterSource implements RequestRouterSource {

    private String serviceId;

    private URI uri;
}
