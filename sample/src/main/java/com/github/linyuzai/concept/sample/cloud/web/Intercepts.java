package com.github.linyuzai.concept.sample.cloud.web;

import com.github.linyuzai.cloud.web.core.concept.Request;
import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import com.github.linyuzai.cloud.web.core.intercept.annotation.BreakIntercept;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnRequest;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnResponse;
import com.github.linyuzai.cloud.web.core.result.WebResult;
import com.github.linyuzai.cloud.web.servlet.WebContextManager;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.method.HandlerMethod;

import java.util.Locale;


@Component
public class Intercepts {

    static {
        WebContextManager.register(new WebContextManager.Listener() {
            @Override
            public void onContextSet(WebContext context) {
                System.out.println("Context Set");
            }

            @Override
            public void onContextInvalid(WebContext context) {
                System.out.println("Context Invalid");
            }
        });
    }

    //@OnWebResponse(Predicate.class)
    /*public boolean onlyTestMap(HttpServletRequest request) {
        return request.getRequestURI().equals("/cloud-web/test-map");
    }*/

    //@OnWebResponse(Predicate.class)
    public boolean onError(WebContext context, Throwable e) {
        if (e != null) {
            context.put(WebResult.class, e);
            return false;
        }
        return true;
    }

    @Order(-1)
    @OnRequest
    public void locale(WebContext context) {
        context.put(Locale.class, Locale.CHINESE);
    }

    @BreakIntercept
    @OnResponse
    public boolean nonWrap(Request request) {
        return request.getPath().equals("/cloud-web/test-map");
    }

    @OnRequest
    @OnResponse
    public void onHttp(HandlerMethod method, WebContext context) {
        System.out.println(context.get(WebInterceptor.Scope.class) + ":" + method.getMethod().getName());
        //System.out.println(method.getMethod().getName());
    }

    @Order(0)
    @OnRequest
    public void request() {
        System.out.println("request");
    }

    @Order(0)
    @OnResponse
    public void response() {
        System.out.println("response");
    }

    /*@Order(WebInterceptor.Orders.PREDICATE + 100)
    @OnWebRequest
    public void checkToken(HttpServletRequest request) {
        String token = request.getHeader("Token");
        if (token == null) {
            throw new RuntimeException("Token not found");
        }
    }*/
}
