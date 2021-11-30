package com.github.linyuzai.download.core.original;

import com.github.linyuzai.download.core.context.DownloadContext;

public interface OriginalSourceFactoryAdapter {

    OriginalSourceFactory getOriginalSourceFactory(Object source, DownloadContext context);
}
