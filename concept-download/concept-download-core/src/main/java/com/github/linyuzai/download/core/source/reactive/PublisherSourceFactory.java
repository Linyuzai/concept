package com.github.linyuzai.download.core.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import org.reactivestreams.Publisher;

/**
 * 匹配 {@link Publisher} 对象的 {@link SourceFactory}。
 */
public class PublisherSourceFactory implements SourceFactory {

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof Publisher;
    }

    @Override
    public Source create(Object source, DownloadContext context) {
        return new PublisherSource((Publisher<?>) source);
    }
}
