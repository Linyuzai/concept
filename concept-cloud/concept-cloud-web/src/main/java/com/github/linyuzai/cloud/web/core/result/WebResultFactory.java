package com.github.linyuzai.cloud.web.core.result;

import com.github.linyuzai.cloud.web.core.context.WebContext;

/**
 * 返回值包装工厂
 */
public interface WebResultFactory {

    /**
     * 创建返回值
     *
     * @param context 上下文
     * @return 返回值
     */
    WebResult<?> create(WebContext context);
}
