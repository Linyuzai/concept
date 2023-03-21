package com.github.linyuzai.cloud.web.core.intercept;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnResponse;
import com.github.linyuzai.cloud.web.core.result.WebResult;
import lombok.*;
import org.springframework.core.MethodParameter;

/**
 * 字符串响应拦截器
 * <p>
 * 若返回类型为字符串则将返回值转为自负床
 */
@Getter
@RequiredArgsConstructor
@OnResponse
public class StringTypeResponseInterceptor implements WebInterceptor {

    private final ObjectMapper objectMapper;

    public StringTypeResponseInterceptor() {
        this(new ObjectMapper());
    }

    @SneakyThrows
    @Override
    public Object intercept(WebContext context, ValueReturner returner, WebInterceptorChain chain) {
        MethodParameter parameter = context.get(MethodParameter.class);
        //如果方法返回值为 String
        if (parameter != null && parameter.getParameterType() == String.class) {
            Object webResult = context.get(WebResult.class);
            //如果 WebResult 不为 null 也不是 String 则转为 String
            if (webResult != null && !(webResult instanceof String)) {
                context.put(WebResult.class, objectMapper.writeValueAsString(webResult));
            }
        }
        return chain.next(context, returner);
    }

    @Override
    public int getOrder() {
        return Orders.STRING_TYPE;
    }
}
