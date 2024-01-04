package com.github.linyuzai.download.core.load.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.load.CompletableFutureSourceLoader;
import com.github.linyuzai.download.core.load.SourceLoader;
import com.github.linyuzai.download.core.load.reactive.ReactiveSourceLoader;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Deprecated
@Getter
@RequiredArgsConstructor
public class ReactorSourceLoader implements ReactiveSourceLoader {

    private final SourceLoader sourceLoader;

    public ReactorSourceLoader() {
        this(new CompletableFutureSourceLoader());
    }

    @Override
    public void load(Source source, DownloadContext context) {
        sourceLoader.load(source, context);
    }
}
