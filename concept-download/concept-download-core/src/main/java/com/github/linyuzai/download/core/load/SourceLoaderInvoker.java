package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;

import java.io.IOException;
import java.util.Collection;

/**
 * 下载源加载器的调用器 / Invoker to invoke SourceLoader
 */
public interface SourceLoaderInvoker {

    /**
     * 调用加载器 / Invoke loader
     *
     * @param loaders 加载器 / Loaders
     * @param context 下载上下文 / Context of download
     * @return 加载结果 / Results of loadings
     * @throws IOException I/O exception
     */
    Collection<SourceLoadResult> invoke(Collection<? extends SourceLoader> loaders, DownloadContext context);
}
