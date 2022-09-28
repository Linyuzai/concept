package com.github.linyuzai.event.local.subscriber;

import com.github.linyuzai.event.core.subscriber.Subscription;
import com.github.linyuzai.event.local.endpoint.LocalEventEndpoint;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 本地订阅句柄
 */
@Getter
@AllArgsConstructor
public class LocalSubscription implements Subscription {

    /**
     * 消息监听器容器
     */
    private LocalEventEndpoint.ListenerContainer container;

    /**
     * 取消订阅
     */
    @Override
    public void unsubscribe() {
        container.remove();
    }
}
