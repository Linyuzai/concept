package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnResponse;
import com.github.linyuzai.cloud.web.core.result.WebResult;
import com.github.linyuzai.cloud.web.core.result.WebResultFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 请求响应拦截器
 * <p>
 * 将响应体包装为 {@link WebResult} 格式
 */
@Getter
@RequiredArgsConstructor
@OnResponse
public class WebResultResponseInterceptor implements WebInterceptor {

    private final WebResultFactory webResultFactory;

    @Override
    public Object intercept(WebContext context, ValueReturner returner, WebInterceptorChain chain) {
        if (!context.containsKey(WebResult.class)) {
            Object body = context.get(WebContext.Response.BODY);
            if (body instanceof WebResult) {
                context.put(WebResult.class, body);
            } else {
                WebResult<?> webResult = webResultFactory.create(context);
                context.put(WebResult.class, webResult);
            }
        }
        return chain.next(context, returner);
    }

    @Override
    public int getOrder() {
        return Orders.WEB_RESULT;
    }
}
