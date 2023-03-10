package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.result.WebResult;

public class WebResultValueReturner implements ValueReturner {

    public static final WebResultValueReturner INSTANCE = new WebResultValueReturner();

    @Override
    public Object value(WebContext context) {
        return context.get(WebResult.class);
    }
}
