package com.github.linyuzai.connection.loadbalance.autoconfigure.redisson;

import com.github.linyuzai.connection.loadbalance.core.concept.AliveForeverConnection;

import com.github.linyuzai.connection.loadbalance.core.message.MessageTransportException;
import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RTopic;
import org.redisson.client.RedisException;

import java.util.Map;
import java.util.function.Consumer;

@Getter
@Setter
public class RedissonTopicConnection extends AliveForeverConnection {

    private Object id;

    private RTopic topic;

    public RedissonTopicConnection(String type) {
        super(type);
    }

    public RedissonTopicConnection(String type, Map<Object, Object> metadata) {
        super(type, metadata);
    }

    @Override
    public void doSend(Object message, Runnable onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        try {
            topic.publish(message);
            onSuccess.run();
        } catch (RedisException e) {
            onError.accept(new MessageTransportException(e));
        } catch (Throwable e) {
            onError.accept(e);
        } finally {
            onComplete.run();
        }
    }
}
