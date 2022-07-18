package com.github.linyuzai.event.core.error;

import com.github.linyuzai.event.core.endpoint.EventEndpoint;

public interface EventErrorHandler {

    void onError(Throwable e, EventEndpoint endpoint);
}
