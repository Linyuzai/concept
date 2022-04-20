package com.github.linyuzai.connection.loadbalance.core.proxy;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnectionFactory;
import com.github.linyuzai.connection.loadbalance.core.message.decode.MessageDecoder;
import com.github.linyuzai.connection.loadbalance.core.message.encode.MessageEncoder;
import lombok.NonNull;

import java.util.Map;

public abstract class ProxyConnectionFactory extends AbstractConnectionFactory {

    public ProxyConnectionFactory(@NonNull MessageEncoder messageEncoder, @NonNull MessageDecoder messageDecoder) {
        super(messageEncoder, messageDecoder);
    }

    @Override
    public boolean support(Map<String, String> metadata) {
        return hasProxyFlag(metadata);
    }
}
