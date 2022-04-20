package com.github.linyuzai.connection.loadbalance.core.proxy;

import com.github.linyuzai.connection.loadbalance.core.event.ConnectionEventListener;

public class ProxyConnectionLogger implements ConnectionEventListener {
    @Override
    public void onEvent(Object event) {
        if (event instanceof ProxyConnectionEvent) {

        }
    }
}
