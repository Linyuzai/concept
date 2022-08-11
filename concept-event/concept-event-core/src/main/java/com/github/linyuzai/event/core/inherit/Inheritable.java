package com.github.linyuzai.event.core.inherit;

import java.util.Map;

/**
 * 可继承的
 */
public interface Inheritable {

    /**
     * 获得所有的端点
     */
    Map<String, ? extends Endpoint> getEndpoints();

    /**
     * 继承或被继承的端点
     */
    interface Endpoint {

        /**
         * 获得继承的端点
         */
        String getInherit();
    }
}
