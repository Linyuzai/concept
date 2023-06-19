package com.github.linyuzai.cloud.web.servlet;

import com.github.linyuzai.cloud.web.core.context.WebContext;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 基于 {@link ThreadLocal} 的上下文管理类
 */
public class WebContextManager {

    //TODO Use InheritableThreadLocal or TransmittableThreadLocal if necessity
    private static final ThreadLocal<WebContext> CONTEXT = new ThreadLocal<>();

    private static final List<Listener> listeners = new CopyOnWriteArrayList<>();

    public static void set(WebContext context) {
        CONTEXT.set(context);
        listeners.forEach(it -> it.onContextSet(context));
    }

    public static WebContext get() {
        return CONTEXT.get();
    }

    public static void invalid() {
        WebContext context = get();
        CONTEXT.remove();
        listeners.forEach(it -> it.onContextInvalid(context));
    }

    public static void register(Listener listener) {
        listeners.add(listener);
    }

    public static void unregister(Listener listener) {
        listeners.remove(listener);
    }

    public interface Listener {

        void onContextSet(WebContext context);

        void onContextInvalid(WebContext context);
    }
}
