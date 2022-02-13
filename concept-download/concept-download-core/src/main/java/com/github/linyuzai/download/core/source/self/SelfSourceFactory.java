package com.github.linyuzai.download.core.source.self;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;

/**
 * 匹配 {@link Source} 的 {@link SourceFactory}。
 */
public class SelfSourceFactory implements SourceFactory {

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof Source;
    }

    @Override
    public Source create(Object source, DownloadContext context) {
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        publisher.publish(new SelfSourceCreatedEvent(context, (Source) source));
        return (Source) source;
    }
}
