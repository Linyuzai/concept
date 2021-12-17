package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;

/**
 * 下载源加载器 / Loader to load download source
 */
public interface SourceLoader {

    /**
     * @return 是否异步加载 / If load async
     */
    boolean isAsyncLoad();

    /**
     * 加载 / load
     *
     * @param context 下载上下文 / Context of download
     * @return 加载结果 / Result of loading
     */
    SourceLoadResult load(DownloadContext context);
}
