package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

import java.io.IOException;

public interface LoadableSource extends Source {

    default void load(DownloadContext context) throws IOException {

    }

    boolean isAsyncLoad();
}
