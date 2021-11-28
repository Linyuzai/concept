package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;

public class DefaultDownloadContextFactory implements DownloadContextFactory {

    @Override
    public DownloadContext create(DownloadOptions options) {
        return new DownloadContext(options);
    }
}
