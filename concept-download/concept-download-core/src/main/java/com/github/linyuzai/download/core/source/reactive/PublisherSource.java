package com.github.linyuzai.download.core.source.reactive;

import com.github.linyuzai.download.core.concept.Part;
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
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

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
        return publisher.toString();
    }

    @Override
    public boolean isAsyncLoad() {
        if (source != null) {
            return source.isAsyncLoad();
        }
        return true;
    }

    @Override
    public boolean isSingle() {
        if (source != null) {
            return source.isSingle();
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Source> list() {
        if (source != null) {
            return source.list();
        }
        return ReactorSource.super.list();
    }

    @Override
    public List<Source> list(Predicate<Source> predicate) {
        if (source != null) {
            return source.list(predicate);
        }
        return ReactorSource.super.list(predicate);
    }

    @Override
    public Collection<Part> getParts() {
        if (source != null) {
            return source.getParts();
        }
        return ReactorSource.super.getParts();
    }

    @Override
    public boolean isCacheEnabled() {
        if (source != null) {
            return source.isCacheEnabled();
        }
        return ReactorSource.super.isCacheEnabled();
    }

    @Override
    public boolean isCacheExisted() {
        if (source != null) {
            return source.isCacheExisted();
        }
        return ReactorSource.super.isCacheExisted();
    }

    @Override
    public String getCachePath() {
        if (source != null) {
            return source.getCachePath();
        }
        return ReactorSource.super.getCachePath();
    }

    @Override
    public void deleteCache() {
        if (source != null) {
            source.deleteCache();
        }
        ReactorSource.super.deleteCache();
    }

    @Override
    public String getPath() {
        if (source != null) {
            return source.getPath();
        }
        return ReactorSource.super.getPath();
    }

    @Override
    public Collection<Part> getChildren() {
        if (source != null) {
            return source.getChildren();
        }
        return ReactorSource.super.getChildren();
    }

    @Override
    public void release() {
        if (source != null) {
            source.release();
        }
        ReactorSource.super.release();
    }

    @Override
    public Mono<Void> preload(DownloadContext context) {
        return create(context).then();
    }

    @Override
    public void load(DownloadContext context) {
        if (source == null) {
            source = create(context).block();
        }

        if (source != null) {
            source.load(context);
        }
    }

    protected Mono<Source> create(DownloadContext context) {
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
                        return Mono.just(source);
                    } else {
                        return Mono.zip(monoList, Function.identity())
                                .map(zip -> source);
                    }
                });
    }
}
