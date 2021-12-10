package com.github.linyuzai.download.core.source.direct;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;

public class DirectSourceFactory implements SourceFactory {

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof Source;
    }

    @Override
    public Source create(Object source, DownloadContext context) {
        return (Source) source;
    }
}
