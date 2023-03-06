package com.github.linyuzai.cloud.web.core.context;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WebContextFactoryImpl implements WebContextFactory {

    @Override
    public WebContext create() {
        return new WebContextImpl();
    }
}
