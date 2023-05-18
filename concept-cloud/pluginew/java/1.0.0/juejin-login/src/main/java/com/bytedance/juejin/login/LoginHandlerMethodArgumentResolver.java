package com.bytedance.juejin.login;

import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

/**
 * 匹配标记了 {@link Login} 的参数处理器
 */
@AllArgsConstructor
public class LoginHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private List<LoginArgumentAdapter> argumentAdapters;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        LoginArgumentAdapter adapter = getArgumentAdapter(parameter);
        if (adapter == null) {
            return null;
        }
        return adapter.adapt(parameter);
    }

    public LoginArgumentAdapter getArgumentAdapter(MethodParameter parameter) {
        for (LoginArgumentAdapter adapter : argumentAdapters) {
            if (adapter.support(parameter)) {
                return adapter;
            }
        }
        return null;
    }
}
