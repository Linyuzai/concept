package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

public interface SourceLoaderFactory {

    SourceLoader create(Source source, DownloadContext context);
}
