package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.DownloadSource;

import java.io.IOException;

public interface DownloadSourceLoader {

    void load(DownloadSource source, DownloadContext context) throws IOException;
}
