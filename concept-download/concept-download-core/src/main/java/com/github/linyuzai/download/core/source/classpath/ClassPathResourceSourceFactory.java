package com.github.linyuzai.download.core.source.classpath;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.Charset;

/**
 * 支持 {@link ClassPathResource} 对象的工厂 / Factory support {@link ClassPathResource}
 */
public class ClassPathResourceSourceFactory implements SourceFactory {

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof ClassPathResource;
    }

    @Override
    public Source create(Object source, DownloadContext context) {
        Charset charset = context.getOptions().getCharset();
        return new ClassPathResourceSource.Builder<>()
                .resource((ClassPathResource) source)
                .charset(charset)
                .build();
    }
}
