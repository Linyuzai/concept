package com.github.linyuzai.download.core.original.file;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.original.prefix.PrefixOriginalSourceFactory;

import java.io.File;
import java.nio.charset.Charset;

public class FilePathOriginalSourceFactory extends PrefixOriginalSourceFactory {

    public static final String[] PREFIXES = new String[]{"file:"};

    @Override
    public OriginalSource create(Object source, DownloadContext context) {
        String path = getContent((String) source);
        Charset charset = context.getOptions().getCharset();
        return new FileOriginalSource.Builder()
                .file(new File(path))
                .charset(charset)
                .build();
    }

    @Override
    protected String[] getPrefixes() {
        return PREFIXES;
    }
}
