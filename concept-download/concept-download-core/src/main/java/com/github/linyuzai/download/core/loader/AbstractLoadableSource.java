package com.github.linyuzai.download.core.loader;

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

@Getter
@Setter
public abstract class AbstractLoadableSource extends AbstractSource {

    private InputStream inputStream;

    public boolean isAsyncLoad() {
        boolean asyncLoad = super.isAsyncLoad();
        if (asyncLoad && isCacheEnabled() && isCacheExisted()) {
            return false;
        }
        return asyncLoad;
    }

    @Override
    public boolean isCacheExisted() {
        return new File(getCachePath(), getName()).exists();
    }

    @Override
    public void load(DownloadContext context) {
        try {
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
        } catch (Throwable e) {
            LoadExceptionHandler handler = context.get(LoadExceptionHandler.class);
            LoadSourceException exception = new LoadSourceException(this, e);
            handler.onLoading(exception);
            Collection<LoadSourceException> exceptions = context.get(LoadSourceException.class);
            exceptions.add(exception);
        }
    }

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
