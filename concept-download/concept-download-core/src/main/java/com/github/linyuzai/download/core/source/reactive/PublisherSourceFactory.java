/*
package com.github.linyuzai.download.core.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.SourceFactoryAdapter;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public class PublisherSourceFactory implements SourceFactory {

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof Publisher;
    }

    @Override
    public Mono<Source> create(Object source, DownloadContext context) {
        SourceFactoryAdapter adapter = context.get(SourceFactoryAdapter.class);
        return Mono.from((Publisher<?>) source)
                .flatMap(it -> adapter.getFactory(it, context).create(it, context));
    }
}
*/
