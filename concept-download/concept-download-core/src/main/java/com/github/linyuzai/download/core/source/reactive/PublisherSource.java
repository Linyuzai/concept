package com.github.linyuzai.download.core.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactoryAdapter;
import com.github.linyuzai.download.core.source.multiple.MultipleSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public class PublisherSource implements ReactorSource {

    private final Publisher<?> publisher;

    private Source source;

    @Override
    public InputStream getInputStream() throws IOException {
        if (source != null) {
            return source.getInputStream();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        if (source != null) {
            return source.getName();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContentType() {
        if (source != null) {
            return source.getContentType();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Charset getCharset() {
        if (source != null) {
            return source.getCharset();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Long getLength() {
        if (source != null) {
            return source.getLength();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDescription() {
        if (source != null) {
            return source.getDescription();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAsyncLoad() {
        if (source != null) {
            return source.isAsyncLoad();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSingle() {
        if (source != null) {
            return source.isSingle();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Mono<Void> preload(DownloadContext context) {
        SourceFactoryAdapter adapter = context.get(SourceFactoryAdapter.class);
        return Flux.from(publisher)
                .map(it -> adapter.getFactory(it, context).create(it, context))
                .collectList()
                .flatMap(it -> {
                    source = new MultipleSource(it);
                    List<Mono<Void>> monoList = new ArrayList<>();
                    for (Source s : it) {
                        if (s instanceof ReactorSource) {
                            monoList.add(((ReactorSource) s).preload(context));
                        }
                    }
                    if (monoList.isEmpty()) {
                        return Mono.empty();
                    } else {
                        return Mono.zip(monoList, Function.identity()).then();
                    }
                });
    }

    @Override
    public void load(DownloadContext context) {
        if (source != null) {
            source.load(context);
        }
    }
}
