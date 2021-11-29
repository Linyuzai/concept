package com.github.linyuzai.download.core.original.file;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.original.OriginalSourceFactory;

import java.io.File;
import java.nio.charset.Charset;

/**
 * 文件数据加载器
 */
public class FileOriginalSourceFactory implements OriginalSourceFactory {

    /**
     * 支持需要下载的数据对象是File类型
     *
     * @param source  需要下载的数据对象
     * @param context 下载上下文
     * @return 是否能加载
     */
    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof File;
    }

    /**
     * @param source  需要下载的数据对象
     * @param context 下载上下文
     * @return {@link FileOriginalSource}
     */
    @Override
    public OriginalSource create(Object source, DownloadContext context) {
        Charset charset = context.getOptions().getCharset();
        return new FileOriginalSource.Builder()
                .file((File) source)
                .charset(charset)
                .build();
    }
}
