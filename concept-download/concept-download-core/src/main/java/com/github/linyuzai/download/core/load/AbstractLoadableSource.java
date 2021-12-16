package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.AbstractSource;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import com.github.linyuzai.download.core.writer.DownloadWriterAdapter;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * 需要预加载资源的抽象类 / Abstract classes that require preloaded resources
 * 需要注意数据流只能使用一次 / Note that stream can only be used once
 */
@Getter
@Setter
public abstract class AbstractLoadableSource extends AbstractSource {

    private InputStream inputStream;

    /**
     * 如果需要异步加载 / If asynchronous loading is required
     * 在启用缓存并存在缓存的情况下 / With cache enabled and cache present
     * 直接同步加载本地缓存 / Loading local cache synchronous
     *
     * @return 是否异步加载 / If async load
     */
    public boolean isAsyncLoad() {
        boolean asyncLoad = super.isAsyncLoad();
        if (asyncLoad && isCacheEnabled() && isCacheExisted()) {
            return false;
        }
        return asyncLoad;
    }

    /**
     * @return 本地文件是否存在
     */
    @Override
    public boolean isCacheExisted() {
        return new File(getCachePath(), getName()).exists();
    }

    /**
     * 不启用缓存，则直接加载 / If caching is not enabled, load directly
     * 启用缓存并缓存存在，则使用缓存 / Cache is used if cache is enabled and exists
     * 启用缓存并缓存不存在，则加载到缓存并使用缓存 / Load into cache and use cache if cache is enabled and cache does not exist,
     *
     * @param context 下载上下文 / Context of download
     */
    @Override
    public void load(DownloadContext context) throws IOException {
        if (isCacheEnabled()) {
            String cachePath = getCachePath();
            if (cachePath == null) {
                throw new DownloadException("Cache path is null");
            }
            File dir = new File(cachePath);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
            }
            File cache = new File(dir, getName());
            if (!cache.exists()) {
                DownloadWriterAdapter writerAdapter = context.get(DownloadWriterAdapter.class);
                DownloadWriter writer = writerAdapter.getWriter(this, null, context);
                try (InputStream is = doLoad(context);
                     FileOutputStream fos = new FileOutputStream(cache)) {
                    writer.write(is, fos, null, getCharset(), getLength());
                }
            }
            inputStream = new FileInputStream(cache);
        } else {
            inputStream = doLoad(context);
        }
    }

    /**
     * 直接使用加载的资源或缓存的资源写入 / Write directly using loaded resources or cached resources
     *
     * @param os      写入数据的输出流 / Output stream to write
     * @param range   写入的范围 / Range of writing
     * @param writer  具体操作字节或字符的处理类 / Handler to handle bytes or chars
     * @param handler 可对每一部分进行单独写入操作 / Do write for each part {@link Part}
     * @throws IOException I/O exception
     */
    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
        if (inputStream == null) {
            return;
        }
        try (InputStream is = inputStream) {
            Part part = new Part() {

                @Override
                public InputStream getInputStream() throws IOException {
                    return is;
                }

                @Override
                public String getName() {
                    return AbstractLoadableSource.this.getName();
                }

                @Override
                public String getPath() {
                    return AbstractLoadableSource.this.getName();
                }

                @Override
                public Charset getCharset() {
                    return AbstractLoadableSource.this.getCharset();
                }

                @Override
                public void write() throws IOException {
                    writer.write(getInputStream(), os, range, getCharset(), AbstractLoadableSource.this.getLength());
                }
            };
            handler.handle(part);
        }
    }

    /**
     * 如果缓存
     */
    @Override
    public void deleteCache() {
        if (isCacheEnabled()) {
            File file = new File(getCachePath(), getName());
            if (file.exists()) {
                boolean delete = file.delete();
            }
        }
    }

    public abstract InputStream doLoad(DownloadContext context) throws IOException;
}
