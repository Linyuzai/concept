package com.github.linyuzai.event.core.subscriber;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组合订阅句柄
 */
@Getter
@AllArgsConstructor
public class ComposeSubscription implements Subscription {

    /**
     * 多个订阅句柄
     */
    private Collection<Subscription> subscriptions;

    /**
     * 遍历所有的订阅句柄进行取消订阅
     */
    @Override
    public void unsubscribe() {
        subscriptions.forEach(Subscription::unsubscribe);
    }

    /**
     * 简化处理
     * <p>
     * 当只有一个句柄时直接返回该句柄
     * <p>
     * 当所有句柄都为空时直接返回空句柄
     * <p>
     * 否则返回一个组合订阅句柄
     */
    public Subscription simplify() {
        List<Subscription> all = new ArrayList<>();
        collect(this, all);
        List<Subscription> list = all.stream()
                .filter(it -> it != Subscription.EMPTY)
                .collect(Collectors.toList());
        int size = list.size();
        if (size == 0) {
            return Subscription.EMPTY;
        } else if (size == 1) {
            return list.get(0);
        } else {
            return new ComposeSubscription(list);
        }
    }

    /**
     * 将所有的组合句柄拆分
     */
    public void collect(Subscription subscription, Collection<Subscription> collection) {
        if (subscription instanceof ComposeSubscription) {
            for (Subscription s : ((ComposeSubscription) subscription).getSubscriptions()) {
                collect(s, collection);
            }
        } else {
            collection.add(subscription);
        }
    }
}
