package com.github.linyuzai.cloud.web.core.context;

import lombok.AllArgsConstructor;

/**
 * 上下文工厂实现类
 */
@AllArgsConstructor
public class WebContextFactoryImpl implements WebContextFactory {

    /**
     * 创建上下文
     *
     * @return {@link WebContextImpl} 实例
     */
    @Override
    public WebContext create() {
        return new WebContextImpl();
    }
}
