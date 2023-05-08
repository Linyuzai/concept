package com.github.linyuzai.cloud.web.servlet;

import com.github.linyuzai.cloud.web.core.concept.Response;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletResponse;

@Getter
@RequiredArgsConstructor
public class ServletResponse implements Response {

    private final HttpServletResponse response;
}
