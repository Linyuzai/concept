package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.source.AbstractSource;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import com.github.linyuzai.download.core.writer.DownloadWriterAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.*;
import java.nio.charset.Charset;

public class OkHttpSource extends AbstractSource {

    private final OkHttpClient client;

    private final String url;

    private ResponseBody body;

    private File cache;

    public OkHttpSource(OkHttpClient client, String url,
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
            DownloadWriterAdapter writerAdapter = context.get(DownloadWriterAdapter.class);
            DownloadWriter writer = writerAdapter.getWriter(this, null, context);
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
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
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
                    return OkHttpSource.this.getName();
                }

                @Override
                public String getPath() {
                    return OkHttpSource.this.getName();
                }

                @Override
                public Charset getCharset() {
                    return OkHttpSource.this.getCharset();
                }

                @Override
                public void write() throws IOException {
                    writer.write(getInputStream(), os, range, getCharset(), OkHttpSource.this.getLength());
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

        public Builder client(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public Builder asyncLoad(boolean asyncLoad) {
            this.asyncLoad = asyncLoad;
            return this;
        }

        public Builder cacheEnabled(boolean cacheEnabled) {
            this.cacheEnabled = cacheEnabled;
            return this;
        }

        public Builder cachePath(String cachePath) {
            this.cachePath = cachePath;
            return this;
        }

        public OkHttpSource build() {
            if (client == null) {
                throw new DownloadException("OkHttpClient is null");
            }
            if (url == null || url.isEmpty()) {
                throw new DownloadException("Url is null or empty");
            }
            if (cacheEnabled && (cachePath == null || cachePath.isEmpty())) {
                throw new DownloadException("Cache path is null or empty");
            }
            return new OkHttpSource(client, url, name, charset, asyncLoad, cacheEnabled, cachePath);
        }
    }
}
