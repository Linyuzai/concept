package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;

public interface SourceLoader {

    boolean isAsyncLoad();

    SourceLoadResult load(DownloadContext context);
}
