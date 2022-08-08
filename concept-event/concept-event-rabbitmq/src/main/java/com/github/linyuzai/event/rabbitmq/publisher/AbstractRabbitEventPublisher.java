package com.github.linyuzai.event.rabbitmq.publisher;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.rabbitmq.binding.RabbitBinding;
import com.github.linyuzai.event.rabbitmq.endpoint.RabbitEventEndpoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRabbitEventPublisher extends RabbitEventPublisher {

    private final Map<String, RabbitBinding> bindings = new ConcurrentHashMap<>();

    @Override
    public void publishRabbit(Object event, RabbitEventEndpoint endpoint, EventContext context) {
        if (!bindings.containsKey(endpoint.getName())) {
            RabbitBinding binding = new RabbitBinding(endpoint.getAdmin());
            binding(binding);
            bindings.put(endpoint.getName(), binding);
        }
        send(event, endpoint, context);
    }

    public void binding(RabbitBinding binding) {

    }

    public abstract void send(Object event, RabbitEventEndpoint endpoint, EventContext context);
}
