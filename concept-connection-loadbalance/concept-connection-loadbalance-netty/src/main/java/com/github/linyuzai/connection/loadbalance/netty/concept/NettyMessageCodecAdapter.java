package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessageCodecAdapter;

/**
 * Netty 消息编解码适配器。
 * <p>
 * Netty message codec adapter.
 */
public class NettyMessageCodecAdapter extends AbstractMessageCodecAdapter {

    public NettyMessageCodecAdapter() {
        addScopes(NettyScoped.NAME);
    }
}
