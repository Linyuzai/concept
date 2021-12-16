package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.load.AbstractLoadableSource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.*;
import java.nio.charset.Charset;

public class OkHttpSource extends AbstractLoadableSource {

    private final OkHttpClient client;

    private final String url;

    public OkHttpSource(OkHttpClient client, String url,
                        String name, Charset charset,
                        boolean asyncLoad, boolean cacheEnabled,
                        String cachePath) {
        this.client = client;
        this.url = url;
        if (name == null || name.isEmpty()) {
            String path;
            if (url.contains("?")) {
                path = url.split("\\?")[0];
            } else {
                path = url;
            }
            int index = path.lastIndexOf("/");
            String substring = path.substring(index + 1);
            if (substring.isEmpty()) {
                setName("NoNameOkHttpSource");
            } else {
                setName(substring);
            }
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
        return 0;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public InputStream doLoad(DownloadContext context) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        if (body == null) {
            throw new DownloadException("Body is null");
        }
        return body.byteStream();
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
