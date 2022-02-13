package com.github.linyuzai.download.core.source.file;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.prefix.PrefixSourceFactory;

import java.io.File;

/**
 * 匹配前缀 'file:' 的 {@link SourceFactory}。
 */
public class FilePrefixSourceFactory extends PrefixSourceFactory {

    public static final String[] PREFIXES = new String[]{"file:"};

    private final SourceFactory factory = new FileSourceFactory();

    @Override
    public Source create(Object source, DownloadContext context) {
        String path = getContent((String) source);
        return factory.create(new File(path), context);
    }

    @Override
    protected String[] getPrefixes() {
        return PREFIXES;
    }
}
