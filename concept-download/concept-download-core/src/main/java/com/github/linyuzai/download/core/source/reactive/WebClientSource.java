package com.github.linyuzai.download.core.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.load.SourceLoadingProgressEvent;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.http.HttpSource;
import com.github.linyuzai.download.core.write.DownloadWriter;
import com.github.linyuzai.download.core.write.DownloadWriterAdapter;
import com.github.linyuzai.download.core.write.Progress;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebClientSource extends HttpSource {

    @Override
    public Mono<Source> doLoad(OutputStream os, DownloadContext context) {
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
                    if (isResponseSuccess(clientResponse.statusCode().value())) {
                        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
                        publisher.publish(new WebClientSourceLoadedEvent(context, WebClientSource.this));
                        return clientResponse.body(new InputStreamBodyExtractor(os, context));
                    } else {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(it -> Mono.error(new DownloadException("code: " + code + ", " + it)));
                    }
                });
    }

    @Override
    public Mono<InputStream> loadRemote(DownloadContext context) {
        return Mono.empty();
    }

    @Override
    public String getDescription() {
        return "WebClientSource(" + url + ")";
    }

    @AllArgsConstructor
    public class InputStreamBodyExtractor implements BodyExtractor<Mono<Source>, ClientHttpResponse> {

        private OutputStream os;

        private DownloadContext context;

        @Override
        public Mono<Source> extract(ClientHttpResponse response, Context ctx) {
            DownloadWriterAdapter writerAdapter = context.get(DownloadWriterAdapter.class);
            DownloadWriter writer = writerAdapter.getWriter(WebClientSource.this, null, context);
            DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
            Progress progress = new Progress(length);
            return Flux.from(response.getBody())
                    .doOnNext(it -> writer.write(it.asInputStream(), os, null, null, length, (current, increase) -> {
                        progress.update(increase);
                        publisher.publish(new SourceLoadingProgressEvent(context, WebClientSource.this, progress.copy()));
                    }))
                    .collectList()
                    .flatMap(it -> {
                        it.forEach(DataBufferUtils::release);
                        return Mono.just(WebClientSource.this);
                    });
            //DataBufferUtils.join(response.getBody()).map(DataBuffer::asInputStream);
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
