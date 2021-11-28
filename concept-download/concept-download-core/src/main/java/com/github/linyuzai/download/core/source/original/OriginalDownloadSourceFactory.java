package com.github.linyuzai.download.core.source.original;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.DownloadSource;
import com.github.linyuzai.download.core.source.DownloadSourceFactory;

public class OriginalDownloadSourceFactory implements DownloadSourceFactory {

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof DownloadSource;
    }

    @Override
    public DownloadSource create(Object source, DownloadContext context) {
        return (DownloadSource) source;
    }
}
