package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnectionSubscriber;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseIdGenerator;
import com.github.linyuzai.connection.loadbalance.sse.concept.SubscriberSseCreateRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;

import java.net.URI;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class ReactiveSseConnectionSubscriber extends SseConnectionSubscriber<ReactiveSubscriberSseConnection> {

    private final SseIdGenerator sseIdGenerator;

    private final ReactiveSseClientFactory sseClientFactory;

    @Override
    public void doSubscribe(URI uri, ConnectionLoadBalanceConcept concept, Consumer<ReactiveSubscriberSseConnection> onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        WebClient client = sseClientFactory.create();
        ReactiveSubscriberSseConnection connection = new ReactiveSubscriberSseConnection();
        Object id = sseIdGenerator.generateId(null);
        connection.setCreateRequest(new SubscriberSseCreateRequest(id));
        Disposable disposable = client.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(String.class)
                .doOnSubscribe(subscription -> onSuccess.accept(connection))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        System.out.println(s);
                    }
                }, onError, onComplete);
        connection.setDisposable(disposable);
    }

    @Override
    public String getType() {
        return "reactive";
    }
}
