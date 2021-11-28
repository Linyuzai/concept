package com.github.linyuzai.download.core.response;

import com.github.linyuzai.download.core.context.DownloadContext;

public interface DownloadResponseProvider {

    DownloadResponse getResponse(DownloadContext context);
}
