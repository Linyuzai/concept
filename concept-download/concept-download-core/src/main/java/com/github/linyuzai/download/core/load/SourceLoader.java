package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import reactor.core.publisher.Mono;

public interface SourceLoader {

    Mono<Source> load(Source source, DownloadContext context);
}
