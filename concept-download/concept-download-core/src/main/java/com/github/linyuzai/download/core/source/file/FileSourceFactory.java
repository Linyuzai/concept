package com.github.linyuzai.download.core.source.file;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;

import java.io.File;
import java.nio.charset.Charset;

/**
 * 匹配 {@link File} 对象的 {@link SourceFactory}。
 */
public class FileSourceFactory implements SourceFactory {

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof File;
    }

    @Override
    public Source create(Object source, DownloadContext context) {
        DownloadOptions options = DownloadOptions.get(context);
        Charset charset = options.getCharset();
        FileSource build = new FileSource.Builder<>()
                .file((File) source)
                .charset(charset)
                .build();
        DownloadEventPublisher publisher = DownloadEventPublisher.get(context);
        publisher.publish(new FileSourceCreatedEvent(context, build));
        return build;
    }
}
