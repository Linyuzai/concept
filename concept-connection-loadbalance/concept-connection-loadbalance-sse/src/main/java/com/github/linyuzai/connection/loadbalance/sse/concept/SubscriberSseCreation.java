package com.github.linyuzai.connection.loadbalance.sse.concept;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubscriberSseCreation implements SseCreation {
    
    private final Object id;

    private final String path;
}
