package com.github.linyuzai.concept.sample.download;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.ValueReturner;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptorChain;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnRequest;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnResponse;
import com.github.linyuzai.download.core.annotation.Download;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
@OnRequest
@OnResponse
public class NotInterceptDownload implements WebInterceptor {

    @Override
    public Object intercept(WebContext context, ValueReturner returner, WebInterceptorChain chain) {
        HandlerMethod method = context.get(HandlerMethod.class);
        if (method != null && method.hasMethodAnnotation(Download.class)) {
            return returner.value(context);
        }
        return chain.next(context, returner);
    }
}
