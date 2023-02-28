package com.github.linyuzai.cloud.web.servlet;

import com.github.linyuzai.cloud.web.context.WebContext;

public class WebContextManager {

    //TODO Use InheritableThreadLocal or TransmittableThreadLocal if necessity
    private static final ThreadLocal<WebContext> CONTEXT = new ThreadLocal<>();

    public static void set(WebContext context) {
        CONTEXT.set(context);
    }

    public static WebContext get() {
        return CONTEXT.get();
    }

    public static void invalid() {
        CONTEXT.remove();
    }
}
