package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.context.DownloadContext;

public enum DownloadMode {

    SYNC, ASYNC;

    public static DownloadMode getMode(DownloadContext context) {
        return context.get(DownloadMode.class);
    }
}
