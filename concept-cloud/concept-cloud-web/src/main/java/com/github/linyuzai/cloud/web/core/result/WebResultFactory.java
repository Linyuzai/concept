package com.github.linyuzai.cloud.web.core.result;

import com.github.linyuzai.cloud.web.core.context.WebContext;

public interface WebResultFactory {

    WebResult<?> create(WebContext context);
}
