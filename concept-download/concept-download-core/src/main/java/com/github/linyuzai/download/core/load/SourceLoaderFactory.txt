package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

/**
 * 加载器工厂 / Factory of SourceLoader
 */
public interface SourceLoaderFactory {

    /**
     * 创建一个加载器 / Create a SourceLoader
     *
     * @param source  下载源 / Source of download
     * @param context 下载上下文 / Context of download
     * @return 加载器 / Loader
     */
    SourceLoader create(Source source, DownloadContext context);
}
