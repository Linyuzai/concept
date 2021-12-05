package com.github.linyuzai.download.classpath;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.original.prefix.PrefixOriginalSourceFactory;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.Charset;

public class ClassPathPrefixOriginalSourceFactory extends PrefixOriginalSourceFactory {

    public static final String[] PREFIXES = new String[]{"classpath:"};

    @Override
    public OriginalSource create(Object source, DownloadContext context) {
        String path = getContent((String) source);
        Charset charset = context.getOptions().getCharset();
        return new ClassPathResourceOriginalSource.Builder()
                .resource(new ClassPathResource(path))
                .charset(charset)
                .build();
    }

    @Override
    protected String[] getPrefixes() {
        return PREFIXES;
    }
}
