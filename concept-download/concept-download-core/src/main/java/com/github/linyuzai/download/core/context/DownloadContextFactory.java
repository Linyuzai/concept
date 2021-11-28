package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;

public interface DownloadContextFactory {

    DownloadContext create(DownloadOptions options);
}
