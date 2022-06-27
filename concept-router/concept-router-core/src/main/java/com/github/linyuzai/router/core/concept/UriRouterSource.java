package com.github.linyuzai.router.core.concept;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URI;

@Getter
@AllArgsConstructor
public class UriRouterSource implements RequestRouterSource {

    private String serviceId;

    private URI uri;
}
