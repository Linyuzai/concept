package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnResponse;
import com.github.linyuzai.cloud.web.core.result.WebResult;
import com.github.linyuzai.cloud.web.core.result.WebResultFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 响应拦截器
 * <p>
 * 将响应体包装为 {@link WebResult} 格式
 */
@Getter
@RequiredArgsConstructor
@OnResponse
public class WebResultResponseInterceptor implements WebInterceptor {

    /**
     * 返回值工厂
     */
    private final WebResultFactory webResultFactory;

    @Override
    public Object intercept(WebContext context, ValueReturner returner, WebInterceptorChain chain) {
        //如果 WebResult 未设置
        if (!context.containsKey(WebResult.class)) {
            //获得 body
            Object body = context.get(WebContext.Response.BODY);
            //如果 body 已经是 WebResult 则直接设置
            if (body instanceof WebResult) {
                context.put(WebResult.class, body);
            } else {
                //生成 WebResult 并设置
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
