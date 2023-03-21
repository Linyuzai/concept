package com.github.linyuzai.cloud.web.core.context;

/**
 * 上下文工厂
 */
public interface WebContextFactory {

    /**
     * 创建上下文
     *
     * @return 创建的上下文
     */
    WebContext create();
}
