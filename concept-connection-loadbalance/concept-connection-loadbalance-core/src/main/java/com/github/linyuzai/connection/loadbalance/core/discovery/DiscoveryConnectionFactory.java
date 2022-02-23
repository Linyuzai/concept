package com.github.linyuzai.connection.loadbalance.core.discovery;

public interface DiscoveryConnectionFactory {

    DiscoveryConnection create(String host, int port);
}
