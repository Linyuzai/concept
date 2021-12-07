package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.original.AbstractOriginalSource;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.writer.SourceWriter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class OkHttpOriginalSource extends AbstractOriginalSource {

    private final OkHttpClient client;

    private final String url;

    private ResponseBody body;

    public OkHttpOriginalSource(OkHttpClient client, String url, String name, Charset charset, boolean asyncLoad) {
        this.client = client;
        this.url = url;
        if (name == null || name.isEmpty()) {
            //TODO
        } else {
            setName(name);
        }
        setCharset(charset);
        setAsyncLoad(asyncLoad);
    }

    @Override
    public long getLength() {
        return body == null ? 0L : body.contentLength();
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public void load() throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        body = response.body();
    }

    @Override
    public void write(OutputStream os, Range range, SourceWriter writer, WriteHandler handler) throws IOException {
        if (body == null) {
            return;
        }
        try (InputStream is = body.byteStream()) {
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
}
