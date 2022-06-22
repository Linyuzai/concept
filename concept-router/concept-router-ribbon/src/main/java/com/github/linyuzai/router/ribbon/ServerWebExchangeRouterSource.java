package com.github.linyuzai.router.ribbon;

import com.github.linyuzai.router.core.concept.RequestRouterSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

@Getter
@AllArgsConstructor
public class ServerWebExchangeRouterSource implements RequestRouterSource {

    @NonNull
    private String serviceId;

    @NonNull
    private ServerWebExchange exchange;

    @Override
    public URI getUri() {
        return exchange.getRequest().getURI();
    }
}
