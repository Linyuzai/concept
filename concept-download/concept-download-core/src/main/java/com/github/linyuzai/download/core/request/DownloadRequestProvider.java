package com.github.linyuzai.download.core.request;

import com.github.linyuzai.download.core.context.DownloadContext;

public interface DownloadRequestProvider {

    DownloadRequest getRequest(DownloadContext context);
}
