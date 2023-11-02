package com.github.linyuzai.download.autoconfigure.source.classpath;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.Charset;

/**
 * 匹配 {@link ClassPathResource} 对象的 {@link SourceFactory}。
 */
public class ClassPathSourceFactory implements SourceFactory {

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof ClassPathResource;
    }

    @Override
    public Source create(Object source, DownloadContext context) {
        Charset charset = context.getOptions().getCharset();
        ClassPathSource build = new ClassPathSource.Builder<>()
                .resource((ClassPathResource) source)
                .charset(charset)
                .build();
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        publisher.publish(new ClassPathSourceCreatedEvent(context, build));
        return build;
    }
}
