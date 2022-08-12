package com.github.linyuzai.event.core.subscriber;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.lifecycle.EventConceptLifecycleListener;
import com.github.linyuzai.event.core.listener.EventListener;
import com.github.linyuzai.event.core.utils.GenericProvider;

/**
 * 事件订阅器的抽象类
 * <p>
 * 处理事件解码
 */
public abstract class AbstractEventSubscriber<E extends EventEndpoint>
        implements EventSubscriber, GenericProvider<E> {

    /**
     * 当适配事件端点时进行订阅
     * <p>
     * 并添加生命周期监听器
     * <p>
     * 当触发销毁时取消监听
     *
     * @param listener 事件监听器
     * @param endpoint 事件端点
     * @param context  事件上下文
     */
    @Override
    public Subscription subscribe(EventListener listener, EventEndpoint endpoint, EventContext context) {
        E adapt = adapt(endpoint, context);
        if (adapt == null) {
            return Subscription.EMPTY;
        }
        Subscription subscription = doSubscribe(listener, adapt, context);
        addLifecycleListener(context, subscription);
        return subscription;
    }

    /**
     * 添加生命周期监听器
     */
    public void addLifecycleListener(EventContext context, Subscription subscription) {
        EventConcept concept = context.get(EventConcept.class);
        concept.addLifecycleListeners(new UnSubscriber(concept, subscription));
    }

    /**
     * 端点适配
     */
    public E adapt(EventEndpoint endpoint, EventContext context) {
        return adaptGeneric(endpoint);
    }

    /**
     * 指定 {@link AbstractEventSubscriber} 上的泛型
     */
    @Override
    public Class<?> getTarget() {
        return AbstractEventSubscriber.class;
    }

    /**
     * 执行订阅
     */
    public abstract Subscription doSubscribe(EventListener listener, E endpoint, EventContext context);

    /**
     * 用于在监听到销毁时取消订阅
     */
    public static class UnSubscriber implements EventConceptLifecycleListener {

        /**
         * 订阅句柄
         */
        private final Subscription subscription;

        public UnSubscriber(EventConcept concept, Subscription subscription) {
            //在取消订阅后移除对应的生命周期监听器
            this.subscription = new PostSubscription(subscription, () ->
                    concept.removeLifecycleListeners(UnSubscriber.this));
        }

        @Override
        public void onInitialize(EventConcept concept) {

        }

        @Override
        public void onDestroy(EventConcept concept) {
            //取消订阅
            subscription.unsubscribe();
        }
    }
}
