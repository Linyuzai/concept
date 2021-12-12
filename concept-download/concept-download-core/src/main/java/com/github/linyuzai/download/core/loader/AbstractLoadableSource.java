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

@Getter
@Setter
public abstract class AbstractLoadableSource extends AbstractSource {

    private boolean asyncLoad;

    private InputStream inputStream;

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
            if (cache.exists()) {
                return;
            }
            DownloadWriterAdapter writerAdapter = context.get(DownloadWriterAdapter.class);
            DownloadWriter writer = writerAdapter.getWriter(this, null, context);
            try (InputStream is = doLoad(context);
                 FileOutputStream fos = new FileOutputStream(cache)) {
                writer.write(is, fos, null, getCharset(), getLength());
            }
            inputStream = new FileInputStream(cache);
        } else {
            inputStream = doLoad(context);
        }
    }

    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
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
