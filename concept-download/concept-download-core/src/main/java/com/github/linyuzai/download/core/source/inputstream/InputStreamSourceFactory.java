package com.github.linyuzai.download.core.source.inputstream;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;

import java.io.InputStream;

/**
 * 支持输入流类型的工厂 / Factory support input stream
 */
public class InputStreamSourceFactory implements SourceFactory {

    /**
     * 支持输入流 / Input stream supported
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 是否支持 / If supported
     */
    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof InputStream;
    }

    /**
     * Use {@link InputStreamSource}
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 下载源 / Source {@link InputStreamSource}
     */
    @Override
    public Source create(Object source, DownloadContext context) {
        return new InputStreamSource((InputStream) source);
    }
}
