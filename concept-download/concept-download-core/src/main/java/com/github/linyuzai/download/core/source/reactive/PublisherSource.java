package com.github.linyuzai.download.core.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactoryAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.nio.charset.Charset;

@Getter
@AllArgsConstructor
public class PublisherSource implements Source {

    private Publisher<?> publisher;

    @Override
    public InputStream getInputStream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContentType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Charset getCharset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Long getLength() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAsyncLoad() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSingle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Mono<Source> load(DownloadContext context) {
        SourceFactoryAdapter adapter = context.get(SourceFactoryAdapter.class);
        return Flux.from(publisher)
                .collectList()
                .map(it -> adapter.getFactory(it, context).create(it, context))
                .flatMap(it -> it.load(context));
    }
}
