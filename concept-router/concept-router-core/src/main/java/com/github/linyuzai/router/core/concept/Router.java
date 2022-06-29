package com.github.linyuzai.router.core.concept;

/**
 * 路由
 */
public interface Router {

    /**
     * 获得唯一ID
     *
     * @return 唯一ID
     */
    String getId();

    /**
     * 是否强制路由
     * <p>
     * 当路由不到时可以采用备用方案
     *
     * @return 是否强制路由
     */
    boolean isForced();

    /**
     * 是否启用
     *
     * @return 是否启用
     */
    boolean isEnabled();

    /**
     * 时间戳，用于排序
     *
     * @return 路由添加或更新的时间戳
     */
    long getTimestamp();

    /**
     * 被路由的来源，如一个HTTP请求
     */
    interface Source {

    }

    /**
     * 路由到的位置，如一个服务实例
     */
    interface Location {

        /**
         * 未匹配到路由
         */
        Location UNMATCHED = new Location() {
        };

        /**
         * 匹配到路由但是位置不可用，无法路由过去
         */
        Location UNAVAILABLE = new Location() {
        };
    }
}
