package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

import java.io.IOException;
import java.util.Collection;

public interface SourceLoader {

    void load(Source source, DownloadContext context) throws IOException;

}
