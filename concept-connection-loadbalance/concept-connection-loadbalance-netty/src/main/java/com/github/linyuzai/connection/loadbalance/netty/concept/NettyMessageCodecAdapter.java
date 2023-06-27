package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.message.AbstractMessageCodecAdapter;

/**
 * Netty 消息编解码适配器
 */
public class NettyMessageCodecAdapter extends AbstractMessageCodecAdapter {

    public NettyMessageCodecAdapter() {
        addScopes(NettyScoped.NAME);
    }
}
