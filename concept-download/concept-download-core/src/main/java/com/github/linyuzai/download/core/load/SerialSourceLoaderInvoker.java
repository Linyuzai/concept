package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 串行的调用器 / Serial loader invoker
 */
public class SerialSourceLoaderInvoker implements SourceLoaderInvoker {

    /**
     * 按顺序调用 / Invoke in order
     *
     * @param loaders 加载器 / Loaders
     * @param context 下载上下文 / Context of download
     * @return 加载结果 / Results of loadings
     * @throws IOException I/O exception
     */
    @Override
    public Collection<SourceLoadResult> invoke(Collection<? extends SourceLoader> loaders, DownloadContext context) throws IOException {
        Collection<SourceLoadResult> results = new ArrayList<>();
        for (SourceLoader loader : loaders) {
            SourceLoadResult result = loader.load(context);
            results.add(result);
        }
        return results;
    }
}
