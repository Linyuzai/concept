package com.github.linyuzai.cloud.web.servlet;

import com.github.linyuzai.cloud.web.core.concept.Request;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@Getter
@RequiredArgsConstructor
public class ServletRequest implements Request {

    private final HttpServletRequest request;

    @Override
    public String getMethod() {
        return request.getMethod();
    }

    @Override
    public String getPath() {
        return request.getRequestURI();
    }

    @Override
    public String getHeader(String name) {
        return request.getHeader(name);
    }
}
