package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.result.WebResult;

/**
 * 响应结果值返回器
 * <p>
 * 在拦截响应时候通过该值返回器获得最终的响应体
 */
public class WebResultValueReturner implements ValueReturner {

    public static final WebResultValueReturner INSTANCE = new WebResultValueReturner();

    @Override
    public Object value(WebContext context) {
        return context.get(WebResult.class);
    }
}
