package com.github.linyuzai.connection.loadbalance.sse.concept;

import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessageCodecAdapter;

/**
 * SSE 消息编解码适配器。
 * <p>
 * SSE message codec adapter.
 */
public abstract class SseMessageCodecAdapter extends AbstractMessageCodecAdapter {

    public SseMessageCodecAdapter() {
        addScopes(SseScoped.NAME);
    }
}
