package com.github.linyuzai.event.rabbitmq.binding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class RabbitBinding {

    private RabbitAdmin admin;

    public QueueConfigurer bind(Queue queue) {
        admin.declareQueue(queue);
        return new QueueConfigurer(queue.getName());
    }

    private static Map<String, Object> createMapForKeys(String... keys) {
        Map<String, Object> map = new HashMap<>();
        for (String key : keys) {
            map.put(key, null);
        }
        return map;
    }

    @AllArgsConstructor
    public class QueueConfigurer {

        protected final String name;

        public void to(FanoutExchange exchange) {
            admin.declareExchange(exchange);
            Binding binding = new Binding(this.name, Binding.DestinationType.QUEUE,
                    exchange.getName(), "", new HashMap<>());
            admin.declareBinding(binding);
        }

        public HeadersExchangeConfigurer to(HeadersExchange exchange) {
            admin.declareExchange(exchange);
            return new HeadersExchangeConfigurer(this, exchange);
        }

        public DirectExchangeConfigurer to(DirectExchange exchange) {
            admin.declareExchange(exchange);
            return new DirectExchangeConfigurer(this, exchange);
        }

        public TopicExchangeConfigurer to(TopicExchange exchange) {
            admin.declareExchange(exchange);
            return new TopicExchangeConfigurer(this, exchange);
        }

        public GenericExchangeConfigurer to(Exchange exchange) {
            admin.declareExchange(exchange);
            return new GenericExchangeConfigurer(this, exchange);
        }
    }

    @AllArgsConstructor
    public class HeadersExchangeConfigurer {

        protected final QueueConfigurer queue;

        protected final HeadersExchange exchange;

        public SingleValueBindingConfigurer where(String key) {
            return new SingleValueBindingConfigurer(key);
        }

        public KeysBindingConfigurer whereAny(String... headerKeys) {
            return new KeysBindingConfigurer(headerKeys, false);
        }

        public MapBindingCreator whereAny(Map<String, Object> headerValues) {
            return new MapBindingCreator(headerValues, false);
        }

        public KeysBindingConfigurer whereAll(String... headerKeys) {
            return new KeysBindingConfigurer(headerKeys, true);
        }

        public MapBindingCreator whereAll(Map<String, Object> headerValues) {
            return new MapBindingCreator(headerValues, true);
        }

        @AllArgsConstructor
        public class SingleValueBindingConfigurer {

            @NonNull
            protected final String key;

            public void exists() {
                Binding binding = new Binding(HeadersExchangeConfigurer.this.queue.name,
                        Binding.DestinationType.QUEUE,
                        HeadersExchangeConfigurer.this.exchange.getName(), "",
                        createMapForKeys(this.key));
                admin.declareBinding(binding);
            }

            public void matches(Object value) {
                Map<String, Object> map = new HashMap<>();
                map.put(this.key, value);
                Binding binding = new Binding(HeadersExchangeConfigurer.this.queue.name,
                        Binding.DestinationType.QUEUE,
                        HeadersExchangeConfigurer.this.exchange.getName(), "", map);
                admin.declareBinding(binding);
            }
        }

        public class KeysBindingConfigurer {

            protected final Map<String, Object> headerMap;

            public KeysBindingConfigurer(String[] headerKeys, boolean matchAll) {
                Assert.notEmpty(headerKeys, "header key list must not be empty");
                this.headerMap = createMapForKeys(headerKeys);
                this.headerMap.put("x-match", (matchAll ? "all" : "any"));
            }

            public void exist() {
                Binding binding = new Binding(HeadersExchangeConfigurer.this.queue.name,
                        Binding.DestinationType.QUEUE,
                        HeadersExchangeConfigurer.this.exchange.getName(), "", this.headerMap);
                admin.declareBinding(binding);
            }
        }

        public class MapBindingCreator {

            protected final Map<String, Object> headerMap;

            public MapBindingCreator(Map<String, Object> headerMap, boolean matchAll) {
                Assert.notEmpty(headerMap, "header map must not be empty");
                this.headerMap = new HashMap<String, Object>(headerMap);
                this.headerMap.put("x-match", (matchAll ? "all" : "any"));
            }

            public void match() {
                Binding binding = new Binding(HeadersExchangeConfigurer.this.queue.name,
                        Binding.DestinationType.QUEUE,
                        HeadersExchangeConfigurer.this.exchange.getName(), "", this.headerMap);
                admin.declareBinding(binding);
            }
        }
    }

    @AllArgsConstructor
    private abstract static class AbstractRoutingKeyConfigurer {

        protected final QueueConfigurer queue;

        protected final String exchange;
    }

    public class TopicExchangeConfigurer extends AbstractRoutingKeyConfigurer {

        public TopicExchangeConfigurer(QueueConfigurer queue, TopicExchange exchange) {
            super(queue, exchange.getName());
        }

        public void with(String routingKey) {
            Binding binding = new Binding(queue.name, Binding.DestinationType.QUEUE, exchange,
                    routingKey, Collections.emptyMap());
            admin.declareBinding(binding);
        }

        public void with(Enum<?> routingKeyEnum) {
            Binding binding = new Binding(queue.name, Binding.DestinationType.QUEUE, exchange,
                    routingKeyEnum.toString(), Collections.emptyMap());
            admin.declareBinding(binding);
        }
    }

    public class GenericExchangeConfigurer extends AbstractRoutingKeyConfigurer {

        public GenericExchangeConfigurer(QueueConfigurer queue, Exchange exchange) {
            super(queue, exchange.getName());
        }

        public ArgumentsConfigurer with(String routingKey) {
            return new ArgumentsConfigurer(this, routingKey);
        }

        public ArgumentsConfigurer with(Enum<?> routingKeyEnum) {
            return new ArgumentsConfigurer(this, routingKeyEnum.toString());
        }

    }

    @AllArgsConstructor
    public class ArgumentsConfigurer {

        protected final GenericExchangeConfigurer configurer;

        protected final String routingKey;

        public void and(Map<String, Object> map) {
            Binding binding = new Binding(this.configurer.queue.name, Binding.DestinationType.QUEUE,
                    this.configurer.exchange, this.routingKey, map);
            admin.declareBinding(binding);
        }

        public void noargs() {
            Binding binding = new Binding(this.configurer.queue.name, Binding.DestinationType.QUEUE,
                    this.configurer.exchange, this.routingKey, Collections.emptyMap());
            admin.declareBinding(binding);
        }
    }

    public class DirectExchangeConfigurer extends AbstractRoutingKeyConfigurer {

        public DirectExchangeConfigurer(QueueConfigurer queue, DirectExchange exchange) {
            super(queue, exchange.getName());
        }

        public void with(String routingKey) {
            Binding binding = new Binding(queue.name, Binding.DestinationType.QUEUE, exchange,
                    routingKey, Collections.emptyMap());
            admin.declareBinding(binding);
        }

        public void with(Enum<?> routingKeyEnum) {
            Binding binding = new Binding(queue.name, Binding.DestinationType.QUEUE, exchange,
                    routingKeyEnum.toString(), Collections.emptyMap());
            admin.declareBinding(binding);
        }

        public void withQueueName() {
            Binding binding = new Binding(queue.name, Binding.DestinationType.QUEUE, exchange,
                    queue.name, Collections.emptyMap());
            admin.declareBinding(binding);
        }
    }
}
