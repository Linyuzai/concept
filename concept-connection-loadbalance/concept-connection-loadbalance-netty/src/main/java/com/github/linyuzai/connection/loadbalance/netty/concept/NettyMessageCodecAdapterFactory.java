package com.github.linyuzai.connection.loadbalance.netty.concept;

import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapter;
import com.github.linyuzai.connection.loadbalance.core.message.MessageCodecAdapterFactory;
import com.github.linyuzai.connection.loadbalance.core.scope.AbstractScoped;

public class NettyMessageCodecAdapterFactory extends AbstractScoped implements MessageCodecAdapterFactory {

    public NettyMessageCodecAdapterFactory() {
        addScopes(NettyScoped.NAME);
    }

    @Override
    public MessageCodecAdapter create(String scope) {
        return new NettyMessageCodecAdapter();
    }
}
