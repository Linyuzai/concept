package com.github.linyuzai.download.autoconfigure.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.http.HttpSource;
import com.github.linyuzai.download.core.source.reactive.ReactorSource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.Map;

/**
 * 使用 {@link WebClient} 处理 http 请求的 {@link Source}。
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebClientSource extends HttpSource implements ReactorSource {

    /**
     * 请求资源。
     *
     * @param context {@link DownloadContext}
     */
    @SneakyThrows
    @Override
    public InputStream loadRemote(DownloadContext context) {
        if (inputStream != null) {
            return inputStream;
        }
        return loadInputStream(context)
                .toFuture()
                .get();
    }

    @Override
    public String getDescription() {
        return "WebClientSource(" + url + ")";
    }

    @Override
    public Mono<Void> preload(DownloadContext context) {
        return loadInputStream(context)
                .flatMap(it -> {
                    inputStream = it;
                    return Mono.empty();
                });
    }

    protected Mono<InputStream> loadInputStream(DownloadContext context) {
        //DownloadEventPublisher publisher = DownloadEventPublisher.get(context);
        //publisher.publish(new LoadWebClientSourceEvent(context, this));
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
                .exchangeToMono(clientResponse -> {
                    clientResponse.headers().contentLength().ifPresent(l -> length = l);
                    int code = clientResponse.statusCode().value();
                    if (isResponseSuccess(code)) {
                        return clientResponse.body(new InputStreamBodyExtractor());
                    } else {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(it -> Mono.error(new DownloadException("code: " + code + ", " + it)));
                    }
                });
    }

    public static class InputStreamBodyExtractor implements BodyExtractor<Mono<InputStream>, ClientHttpResponse> {

        @Override
        public Mono<InputStream> extract(ClientHttpResponse response, Context ctx) {
            return DataBufferUtils.join(response.getBody())
                    .map(it -> it.asInputStream(true));
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
