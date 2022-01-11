package com.github.linyuzai.download.core.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.http.HttpSource;
import lombok.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebClientSource extends HttpSource {

    @SuppressWarnings("all")
    @Override
    public Mono<InputStream> doLoad(DownloadContext context) {
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
                .retrieve()
                .bodyToMono(byte[].class)
                .map(ByteArrayInputStream::new);
    }

    @Override
    public String toString() {
        return "WebClientSource{" +
                "url='" + url + '\'' +
                '}';
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
