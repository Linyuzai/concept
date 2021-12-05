package com.github.linyuzai.download.classpath;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.original.OriginalSourceFactory;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.Charset;

public class ClassPathResourceOriginalSourceFactory implements OriginalSourceFactory {

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof ClassPathResource;
    }

    @Override
    public OriginalSource create(Object source, DownloadContext context) {
        Charset charset = context.getOptions().getCharset();
        return new ClassPathResourceOriginalSource.Builder()
                .resource((ClassPathResource) source)
                .charset(charset)
                .build();
    }
}
