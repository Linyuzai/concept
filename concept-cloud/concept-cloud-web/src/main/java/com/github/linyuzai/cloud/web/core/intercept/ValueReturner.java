package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;

public interface ValueReturner {

    Object value(WebContext context);
}
