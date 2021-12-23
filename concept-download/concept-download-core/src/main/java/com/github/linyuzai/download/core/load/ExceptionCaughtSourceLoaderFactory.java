package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;

/**
 * 处理了异常的 加载器 工厂 / Factory of loader which catches exceptions
 */
@AllArgsConstructor
public class ExceptionCaughtSourceLoaderFactory implements SourceLoaderFactory {

    private SourceLoadExceptionHandler handler;

    /**
     * 创建一个处理了异常的加载器 / Create a SourceLoader which handling exception
     *
     * @param source  下载源 / Source of download
     * @param context 下载上下文 / Context of download
     * @return 处理了异常的 加载器 / Loader which handling exception
     */
    @Override
    public SourceLoader create(Source source, DownloadContext context) {
        return new ExceptionCaughtSourceLoader(source, handler);
    }
}
