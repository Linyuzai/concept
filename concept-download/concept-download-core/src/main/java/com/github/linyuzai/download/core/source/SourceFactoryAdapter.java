package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.context.DownloadContext;

public interface SourceFactoryAdapter {

    SourceFactory getOriginalSourceFactory(Object source, DownloadContext context);
}
