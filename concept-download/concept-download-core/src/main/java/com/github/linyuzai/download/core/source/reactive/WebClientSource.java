package com.github.linyuzai.download.core.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.source.http.HttpSource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Function;

@CommonsLog
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebClientSource extends HttpSource {

    @SuppressWarnings("all")
    @Override
    public Mono<InputStream> loadRemote(DownloadContext context) {
        context.log("[Load source] " + this + " will be load by WebClient(webflux)");
        return WebClient.create()
                .get()
                .uri(url)
                .headers(httpHeaders -> {
                    if (headers != null) {
                        for (Map.Entry<String, String> entry : headers.entrySet()) {
                            httpHeaders.add(entry.getKey(), entry.getValue());
                        }
                    }
                })
                .exchangeToMono(new Function<ClientResponse, Mono<InputStream>>() {
                    @Override
                    public Mono<InputStream> apply(ClientResponse clientResponse) {
                        int code = clientResponse.statusCode().value();
                        if (isResponseSuccess(clientResponse.statusCode().value())) {
                            return clientResponse.body(new InputStreamBodyExtractor());
                        } else {
                            return clientResponse.bodyToMono(String.class).flatMap(it -> {
                                return Mono.error(new DownloadException("code: " + code + ", " + it));
                            });
                        }
                    }
                });
    }

    @Override
    public String toString() {
        return "WebClientSource{" +
                "url='" + url + '\'' +
                '}';
    }

    public static class InputStreamBodyExtractor implements BodyExtractor<Mono<InputStream>, ClientHttpResponse> {

        @Override
        public Mono<InputStream> extract(ClientHttpResponse response, Context context) {
            return DataBufferUtils.join(response.getBody()).map(DataBuffer::asInputStream);
        }
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends WebClientSource, B extends Builder<T, B>> extends HttpSource.Builder<T, B> {

        @Override
        protected T build(T target) {
            return super.build(target);
        }

        @Override
        public T build() {
            return build((T) new WebClientSource());
        }
    }
}
