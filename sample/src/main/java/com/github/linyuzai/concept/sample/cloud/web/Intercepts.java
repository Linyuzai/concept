package com.github.linyuzai.concept.sample.cloud.web;

import com.github.linyuzai.cloud.web.core.context.WebContext;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnRequest;
import com.github.linyuzai.cloud.web.core.intercept.annotation.OnResponse;
import com.github.linyuzai.cloud.web.core.result.WebResult;
import org.springframework.stereotype.Component;

//import javax.servlet.http.HttpServletRequest;


@Component
public class Intercepts {

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

    /*@OnWebRequest
    @OnWebResponse
    @OnWebError
    public void onHttp(HandlerMethod method) {
        System.out.println("http");
        //System.out.println(method.getMethod().getName());
    }*/

    @OnRequest
    public void request() {
        System.out.println("request");
    }

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
