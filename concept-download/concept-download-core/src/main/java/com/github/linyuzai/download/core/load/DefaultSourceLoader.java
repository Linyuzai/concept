package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import reactor.core.publisher.Mono;

public class DefaultSourceLoader implements SourceLoader {

    @Override
    public Mono<Source> load(Source source, DownloadContext context) {
        return Mono.just(source)
                .flatMap(it -> it.load(context));
    }
}
