package com.github.linyuzai.event.rabbitmq.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.rabbitmq.binding.RabbitBinding;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RabbitMQ 事件发布器抽象类
 */
public abstract class AbstractRabbitEventPublisher extends RabbitEventPublisher {

    /**
     * Binding缓存
     */
    private final Map<String, RabbitBinding> bindings = new ConcurrentHashMap<>();

    /**
     * 发布前先进行 Binding 创建
     */
    @Override
    public void doPublish(Object event, RabbitEventEndpoint endpoint, EventContext context) {
        if (!bindings.containsKey(endpoint.getName())) {
            RabbitBinding binding = new RabbitBinding(endpoint.getAdmin());
            binding(binding);
            bindings.put(endpoint.getName(), binding);
        }
        send(event, endpoint, context);
    }

    /**
     * 用于创建 交换机/队列/绑定关系
     */
    public void binding(RabbitBinding binding) {

    }

    public abstract void send(Object event, RabbitEventEndpoint endpoint, EventContext context);
}
