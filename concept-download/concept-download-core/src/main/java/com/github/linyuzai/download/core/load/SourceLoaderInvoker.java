package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;

import java.io.IOException;
import java.util.Collection;

/**
 * 下载源加载器
 */
public interface SourceLoaderInvoker {

    /**
     * 加载资源 / Load resource
     *
     * @param source  需要加载的下载源 / Source need load
     * @param context 下载上下文 / Context of download
     * @throws IOException I/O exception
     */
    Collection<SourceLoadResult> invoke(Collection<? extends SourceLoader> loaders, DownloadContext context) throws IOException;
}
