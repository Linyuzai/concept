package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.original.AbstractOriginalSource;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.writer.SourceWriter;
import com.github.linyuzai.download.core.writer.SourceWriterAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.*;
import java.nio.charset.Charset;

public class OkHttpOriginalSource extends AbstractOriginalSource {

    private final OkHttpClient client;

    private final String url;

    private ResponseBody body;

    private File cache;

    public OkHttpOriginalSource(OkHttpClient client, String url,
                                String name, Charset charset,
                                boolean asyncLoad, boolean cacheEnabled,
                                String cachePath) {
        this.client = client;
        this.url = url;
        if (name == null || name.isEmpty()) {
            //TODO
        } else {
            setName(name);
        }
        setCharset(charset);
        setAsyncLoad(asyncLoad);
        setCacheEnabled(cacheEnabled);
        setCachePath(cachePath);
    }

    @Override
    public long getLength() {
        if (cache != null) {
            return cache.length();
        } else {
            return body == null ? 0L : body.contentLength();
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public void load(DownloadContext context) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        body = response.body();
        if (isCacheEnabled()) {
            String cachePath = getCachePath();
            if (cachePath == null) {
                throw new DownloadException("Cache path is null");
            }
            File dir = new File(cachePath);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
            }
            cache = new File(dir, getName());
            SourceWriterAdapter writerAdapter = context.get(SourceWriterAdapter.class);
            SourceWriter writer = writerAdapter.getSourceWriter(this, null, context);
            try (InputStream is = body.byteStream();
                 FileOutputStream fos = new FileOutputStream(cache)) {
                writer.write(is, fos, null, getCharset(), getLength());
            }
        }
    }

    @Override
    public void deleteCache() {
        if (cache != null) {
            boolean delete = cache.delete();
        }
    }

    @Override
    public void write(OutputStream os, Range range, SourceWriter writer, WriteHandler handler) throws IOException {
        InputStream inputStream;
        if (isCacheEnabled()) {
            if (cache == null) {
                return;
            }
            inputStream = new FileInputStream(cache);
        } else {
            if (body == null) {
                return;
            }
            inputStream = body.byteStream();
        }

        try (InputStream is = inputStream) {
            Part part = new Part() {

                @Override
                public InputStream getInputStream() throws IOException {
                    return is;
                }

                @Override
                public String getName() {
                    return OkHttpOriginalSource.this.getName();
                }

                @Override
                public String getPath() {
                    return OkHttpOriginalSource.this.getName();
                }

                @Override
                public Charset getCharset() {
                    return OkHttpOriginalSource.this.getCharset();
                }

                @Override
                public void write() throws IOException {
                    writer.write(getInputStream(), os, range, getCharset(), OkHttpOriginalSource.this.getLength());
                }
            };
            handler.handle(part);
        }
    }

    public static class Builder {

        private OkHttpClient client;

        private String url;

        private String name;

        private Charset charset;

        private boolean asyncLoad = true;

        private boolean cacheEnabled = true;

        private String cachePath;

        public OkHttpOriginalSource build() {
            return new OkHttpOriginalSource(client, url, name, charset, asyncLoad, cacheEnabled, cachePath);
        }
    }
}
