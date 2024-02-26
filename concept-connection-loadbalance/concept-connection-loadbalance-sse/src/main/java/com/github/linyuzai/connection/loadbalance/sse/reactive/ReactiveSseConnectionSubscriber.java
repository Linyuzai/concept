package com.github.linyuzai.connection.loadbalance.sse.reactive;

import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.sse.concept.SseConnectionSubscriber;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class ReactiveSseConnectionSubscriber extends SseConnectionSubscriber<ReactiveSseConnection> {

    private final ReactiveSseClientFactory sseClientFactory;

    @Override
    public void doSubscribe(URI uri, ConnectionLoadBalanceConcept concept, Consumer<ReactiveSseConnection> onSuccess, Consumer<Throwable> onError, Runnable onComplete) {
        WebClient webClient = sseClientFactory.create();
        webClient.get()
                .uri(uri).retrieve()
                .bodyToFlux(String.class)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        System.out.println(s);
                    }
                }, onError, onComplete);
    }

    @Override
    public String getType() {
        return "reactive";
    }
}
