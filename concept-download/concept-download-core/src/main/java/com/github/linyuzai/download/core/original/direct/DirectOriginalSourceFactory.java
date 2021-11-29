package com.github.linyuzai.download.core.original.direct;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.original.OriginalSourceFactory;

public class DirectOriginalSourceFactory implements OriginalSourceFactory {

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof OriginalSource;
    }

    @Override
    public OriginalSource create(Object source, DownloadContext context) {
        return (OriginalSource) source;
    }
}
