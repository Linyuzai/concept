package com.github.linyuzai.download.core.handler;

import com.github.linyuzai.download.core.context.DownloadContext;

public interface DownloadHandlerInterceptor {

    void intercept(DownloadHandler handler, DownloadContext context);
}
