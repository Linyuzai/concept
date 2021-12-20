package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.load.AbstractLoadableSource;
import lombok.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.*;

/**
 * 使用OkHttp加载资源 / Use OkHttp to load source
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OkHttpSource extends AbstractLoadableSource {

    @Setter
    @NonNull
    protected OkHttpClient client;

    @NonNull
    protected final String url;

    /**
     * 如果没有指定名称 / If no name is specified
     * 将截取url最后一段作为名称 / Take the last paragraph of the intercepted URL as the name
     *
     * @return 名称 / Name
     */
    @Override
    public String getName() {
        String name = super.getName();
        if (name == null || name.isEmpty()) {
            String path;
            if (url.contains("?")) {
                path = url.split("\\?")[0];
            } else {
                path = url;
            }
            int index = path.lastIndexOf("/");
            String substring = path.substring(index + 1);
            if (!substring.isEmpty()) {
                setName(substring);
            }
        }
        return super.getName();
    }

    /**
     * 返回0 / return 0
     *
     * @return 0
     */
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
        if (isResponseSuccess(response)) {
            ResponseBody body = response.body();
            if (body == null) {
                throw new DownloadException("Body is null");
            }
            return body.byteStream();
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(response.code()).append(";");
            String message = response.message();
            if (!message.isEmpty()) {
                builder.append(message).append(";");
            }
            ResponseBody body = response.body();
            if (body != null) {
                String s = body.string();
                if (!s.isEmpty()) {
                    builder.append(s).append(";");
                }
            }
            throw new DownloadException(builder.toString());
        }
    }

    protected boolean isResponseSuccess(Response response) {
        return response.code() == 200;
    }

    @Override
    public String toString() {
        return "OkHttpSource{" +
                "url='" + url + '\'' +
                '}';
    }

    public static class Builder extends AbstractLoadableSource.Builder<OkHttpSource, Builder> {

        private OkHttpClient client;

        private String url;

        public Builder() {
            asyncLoad = true;
            cacheEnabled = true;
        }

        public Builder client(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
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
            return super.build(new OkHttpSource(client, url));
        }
    }
}
