package com.github.linyuzai.download.core.source.classpath;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.prefix.PrefixSourceFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * ClassPath前缀 'classpath:' 的工厂 / Factory for class path prefix 'classpath:'
 */
public class ClassPathPrefixSourceFactory extends PrefixSourceFactory {

    public static final String[] PREFIXES = new String[]{"classpath:"};

    private final SourceFactory factory = new ClassPathResourceSourceFactory();

    @Override
    public Source create(Object source, DownloadContext context) {
        String path = getContent((String) source);
        return factory.create(new ClassPathResource(path), context);
    }

    @Override
    protected String[] getPrefixes() {
        return PREFIXES;
    }
}
