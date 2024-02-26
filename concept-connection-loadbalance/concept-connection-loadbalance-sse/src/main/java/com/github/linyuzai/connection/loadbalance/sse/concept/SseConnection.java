package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SseConnection extends AliveForeverConnection {

    private SseCreateRequest createRequest;

    @Override
    public Object getId() {
        return createRequest.getId();
    }

    public String getPath() {
        return createRequest.getPath();
    }
}
