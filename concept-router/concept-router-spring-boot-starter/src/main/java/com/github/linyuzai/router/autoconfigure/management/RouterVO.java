package com.github.linyuzai.router.autoconfigure.management;

import lombok.Data;

/**
 * 路由视图
 */
@Data
public class RouterVO {

    /**
     * 路由ID
     */
    private String id;

    /**
     * 服务ID
     */
    private String serviceId;

    /**
     * 路径匹配模式
     */
    private String pathPattern;

    /**
     * 服务地址
     */
    private String serverAddress;

    /**
     * 强制路由
     */
    private Boolean forced;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
