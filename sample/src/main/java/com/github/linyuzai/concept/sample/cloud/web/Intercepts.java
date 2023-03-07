package com.github.linyuzai.concept.sample.cloud.web;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnWebError;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnWebRequest;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnWebResponse;
import com.github.linyuzai.cloud.web.core.result.WebResult;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Predicate;

@Component
public class Intercepts {

    //@OnWebResponse(Predicate.class)
    public boolean onlyTestMap(HttpServletRequest request) {
        return request.getRequestURI().equals("/cloud-web/test-map");
    }

    //@OnWebResponse(Predicate.class)
    public boolean onError(WebContext context, Throwable e) {
        if (e != null) {
            context.put(WebResult.class, e);
            return false;
        }
        return true;
    }

    @OnWebRequest
    @OnWebResponse
    @OnWebError
    public void onHttp(HandlerMethod method) {
        System.out.println(method.getMethod().getName());
    }
}
