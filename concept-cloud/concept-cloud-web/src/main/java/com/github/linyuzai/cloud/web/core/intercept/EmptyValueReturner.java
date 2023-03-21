package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;

/**
 * webmvc 返回 null
 */
public class EmptyValueReturner implements ValueReturner {

    public static final EmptyValueReturner INSTANCE = new EmptyValueReturner();

    @Override
    public Object value(WebContext context) {
        return null;
    }
}
