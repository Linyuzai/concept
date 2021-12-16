package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

import java.io.IOException;
import java.util.Collection;

/**
 * 下载源加载器
 */
public interface SourceLoader {

    /**
     * 加载资源 / Load resource
     *
     * @param source  需要加载的下载源 / Source need load
     * @param context 下载上下文 / Context of download
     * @throws IOException I/O exception
     */
    void load(Source source, DownloadContext context) throws IOException;
}
