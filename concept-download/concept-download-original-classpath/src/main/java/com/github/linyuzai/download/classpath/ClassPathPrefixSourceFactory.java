package com.github.linyuzai.download.classpath;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.prefix.PrefixSourceFactory;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.Charset;

public class ClassPathPrefixSourceFactory extends PrefixSourceFactory {

    public static final String[] PREFIXES = new String[]{"classpath:"};

    @Override
    public Source create(Object source, DownloadContext context) {
        String path = getContent((String) source);
        Charset charset = context.getOptions().getCharset();
        return new ClassPathResourceSource.Builder()
                .resource(new ClassPathResource(path))
                .charset(charset)
                .build();
    }

    @Override
    protected String[] getPrefixes() {
        return PREFIXES;
    }
}
