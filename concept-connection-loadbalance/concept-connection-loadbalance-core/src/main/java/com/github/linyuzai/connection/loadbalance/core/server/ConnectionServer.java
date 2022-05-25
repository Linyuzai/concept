package com.github.linyuzai.connection.loadbalance.core.server;

import java.net.URI;
import java.util.Map;

/**
 * 连接服务
 * <p>
 * 表示服务实例信息
 */
public interface ConnectionServer {

    /**
     * 获得服务实例 id
     *
     * @return 服务实例 id
     */
    String getInstanceId();

    /**
     * 获得服务 id
     *
     * @return 服务 id
     */
    String getServiceId();

    /**
     * 获得 host
     *
     * @return host
     */
    String getHost();

    /**
     * 获得 port
     *
     * @return port
     */
    int getPort();

    /**
     * 获得元数据
     *
     * @return 元数据
     */
    Map<String, String> getMetadata();

    /**
     * 获得 {@link URI}
     *
     * @return {@link URI}
     */
    URI getUri();

    /**
     * 获得 Scheme
     *
     * @return Scheme
     */
    String getScheme();

    /**
     * 是否安全的
     *
     * @return 是否安全的
     */
    boolean isSecure();
}
