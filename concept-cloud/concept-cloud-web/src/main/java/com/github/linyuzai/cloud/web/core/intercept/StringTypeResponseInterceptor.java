package com.github.linyuzai.cloud.web.core.intercept;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.cloud.web.core.CloudWebException;
import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnResponse;
import com.github.linyuzai.cloud.web.core.result.WebResult;
import lombok.*;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;

/**
 * 字符串响应拦截器
 * 若响应体为字符串则设置设置响应实体内容，且为 {@link String} 类型
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
        Method method = parameter.getMethod();
        if (method != null && method.getReturnType() == String.class) {
            WebResult<?> webResult = context.get(WebResult.class);
            if (webResult == null) {
                throw new CloudWebException("WebResult not found");
            }
            context.put(WebResult.class, objectMapper.writeValueAsString(webResult));
        }
        return chain.next(context, returner);
    }

    @Override
    public int getOrder() {
        return Order.STRING_TYPE;
    }
}
