package com.github.linyuzai.cloud.web.result;

import com.github.linyuzai.cloud.web.context.WebContext;

public interface WebResultFactoryAdapter {

    WebResultFactory getWebResultFactory(WebContext context);
}
