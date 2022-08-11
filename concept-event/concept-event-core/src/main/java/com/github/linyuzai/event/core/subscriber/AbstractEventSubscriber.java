package com.github.linyuzai.event.core.subscriber;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.lifecycle.EventConceptLifecycleListener;
import com.github.linyuzai.event.core.listener.EventListener;

/**
 * 事件订阅器的抽象类
 * <p>
 * 处理事件解码
 */
public abstract class AbstractEventSubscriber implements EventSubscriber {

    /**
     * 当支持订阅对应的事件端点时进行订阅
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
        if (support(endpoint, context)) {
            Subscription subscription = doSubscribe(listener, endpoint, context);
            addLifecycleListener(context, subscription);
            return subscription;
        } else {
            return Subscription.EMPTY;
        }
    }

    /**
     * 添加生命周期监听器¬
     */
    public void addLifecycleListener(EventContext context, Subscription subscription) {
        EventConcept concept = context.get(EventConcept.class);
        concept.addLifecycleListeners(new UnSubscriber(concept, subscription));
    }

    /**
     * 是否支持订阅该节点
     */
    public abstract boolean support(EventEndpoint endpoint, EventContext context);

    /**
     * 执行订阅
     */
    public abstract Subscription doSubscribe(EventListener listener, EventEndpoint endpoint, EventContext context);

    /**
     * 用于在监听到销毁时取消订阅
     */
    public static class UnSubscriber implements EventConceptLifecycleListener {

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
