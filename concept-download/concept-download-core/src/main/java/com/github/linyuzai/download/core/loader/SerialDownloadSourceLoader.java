package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.DownloadSource;

import java.io.IOException;

public class SerialDownloadSourceLoader implements DownloadSourceLoader {

    @Override
    public void load(DownloadSource source, DownloadContext context) throws IOException {
        source.load();
    }
}
