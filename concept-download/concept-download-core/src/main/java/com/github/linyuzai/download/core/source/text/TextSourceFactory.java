package com.github.linyuzai.download.core.source.text;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;

import java.nio.charset.Charset;

/**
 * 匹配任意文本的 {@link Source}。
 */
public class TextSourceFactory implements SourceFactory {

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof String;
    }

    @Override
    public Source create(Object source, DownloadContext context) {
        Charset charset = context.getOptions().getCharset();
        TextSource build = new TextSource.Builder<>()
                .text((String) source)
                .name("text.txt")
                .charset(charset)
                .build();
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        publisher.publish(new TextSourceCreatedEvent(context, build));
        return build;
    }
}
