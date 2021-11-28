package com.github.linyuzai.download.core.source.file;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.DownloadSource;
import com.github.linyuzai.download.core.source.prefix.PrefixDownloadSourceFactory;

import java.io.File;

public class FilePathDownloadSourceFactory extends PrefixDownloadSourceFactory {

    public static final String[] PREFIXES = new String[]{"file:"};

    @Override
    public DownloadSource create(Object source, DownloadContext context) {
        String path = getContent((String) source);
        return new FileDownloadSource.Builder().file(new File(path)).build();
    }

    @Override
    protected String[] getPrefixes() {
        return PREFIXES;
    }
}
