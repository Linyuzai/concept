package com.github.linyuzai.connection.loadbalance.sse.servlet.okhttp;

import com.github.linyuzai.connection.loadbalance.sse.concept.DefaultSseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnectionSubscriberFactory;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseIdGenerator;
import lombok.Getter;
import lombok.Setter;

/**
 * OkHttp SSE 连接订阅器工厂。
 * <p>
 * OkHttp SSE connection subscriber factory.
 */
@Deprecated
@Getter
@Setter
public class OkHttpSseConnectionSubscriberFactory extends SseConnectionSubscriberFactory<OkHttpSseConnection> {

    private SseIdGenerator sseIdGenerator = new DefaultSseIdGenerator();

    private OkHttpSseClientFactory sseClientFactory;

    @Override
    public SseConnectionSubscriber<OkHttpSseConnection> doCreate(String scope) {
        return new OkHttpSseConnectionSubscriber(sseIdGenerator, sseClientFactory);
    }
}
