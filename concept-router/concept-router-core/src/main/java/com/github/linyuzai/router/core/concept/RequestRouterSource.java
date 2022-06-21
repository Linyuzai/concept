package com.github.linyuzai.router.core.concept;

import java.net.URI;

public interface RequestRouterSource extends Router.Source {

    String getServiceId();

    URI getUri();
}
