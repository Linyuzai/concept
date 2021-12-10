package com.github.linyuzai.download.core.source.file;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.prefix.PrefixSourceFactory;

import java.io.File;
import java.nio.charset.Charset;

public class FilePrefixSourceFactory extends PrefixSourceFactory {

    public static final String[] PREFIXES = new String[]{"file:"};

    @Override
    public Source create(Object source, DownloadContext context) {
        String path = getContent((String) source);
        Charset charset = context.getOptions().getCharset();
        return new FileSource.Builder()
                .file(new File(path))
                .charset(charset)
                .build();
    }

    @Override
    protected String[] getPrefixes() {
        return PREFIXES;
    }
}
