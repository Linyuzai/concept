package com.github.linyuzai.cloud.web.result;

import com.github.linyuzai.cloud.web.CloudWebException;
import com.github.linyuzai.cloud.web.context.WebContext;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class WebResultFactoryAdapterImpl implements WebResultFactoryAdapter {

    private List<WebResultFactory> webResultFactories;

    @Override
    public WebResultFactory getWebResultFactory(WebContext context) {
        for (WebResultFactory factory : webResultFactories) {
            if (factory.support(context)) {
                return factory;
            }
        }
        throw new CloudWebException("No WebResultFactory supported");
    }
}
