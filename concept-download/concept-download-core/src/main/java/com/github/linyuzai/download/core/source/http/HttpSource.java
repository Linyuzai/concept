package com.github.linyuzai.download.core.source.http;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.load.RemoteLoadableSource;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.DownloadWriter;
import com.github.linyuzai.download.core.write.DownloadWriterAdapter;
import lombok.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 使用 {@link HttpURLConnection} 处理 http 请求的 {@link Source}。
 */
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HttpSource extends RemoteLoadableSource {

    @NonNull
    protected String url;

    protected int connectTimeout = 5000;

    protected int readTimeout = 5000;

    protected Map<String, String> headers;

    /**
     * 如果没有指定名称则截取 url 最后一段作为名称。
     *
     * @return 指定的名称或 url 截取的最后一段
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
     * 直接返回 true。
     *
     * @return true
     */
    @Override
    public boolean isSingle() {
        return true;
    }

    /**
     * 使用 {@link HttpURLConnection} 请求资源。
     *
     * @param context {@link DownloadContext}
     * @return {@link HttpURLConnection#getInputStream()}
     */
    @Override
    public InputStream loadRemote(DownloadContext context) throws IOException {
        //DownloadEventPublisher publisher = DownloadEventPublisher.get(context);
        //publisher.publish(new LoadHttpSourceEvent(context, this));
        HttpURLConnection connection = getConnection();
        int code = connection.getResponseCode();
        if (isResponseSuccess(code)) {
            String contentType = getContentType();
            if (contentType == null || contentType.isEmpty()) {
                String ct = connection.getContentType();
                if (ct != null) {
                    setContentType(ct);
                }
            }
            long l = connection.getContentLength();
            if (l != -1) {
                length = l;
            }
            return connection.getInputStream();
        } else {
            DownloadWriterAdapter writerAdapter = context.get(DownloadWriterAdapter.class);
            DownloadWriter writer = writerAdapter.getWriter(this, context);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writer.write(connection.getErrorStream(), os, null, null, null);
            throw new DownloadException("code: " + code + ", " + os.toString());
        }
    }

    private HttpURLConnection getConnection() throws IOException {
        URL u = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        connection.setConnectTimeout(connectTimeout);
        connection.setReadTimeout(readTimeout);
        connection.connect();
        return connection;
    }

    /**
     * 请求是否成功。
     *
     * @param code 状态码
     * @return 如果请求成功则返回 true
     */
    protected boolean isResponseSuccess(int code) {
        return code == HttpURLConnection.HTTP_OK ||
                code == HttpURLConnection.HTTP_CREATED ||
                code == HttpURLConnection.HTTP_ACCEPTED;
    }

    @Override
    public String getDescription() {
        return "HttpSource(" + url + ")";
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends HttpSource, B extends Builder<T, B>> extends RemoteLoadableSource.Builder<T, B> {

        protected String url;

        protected Map<String, String> headers;

        public B url(String url) {
            this.url = url;
            return (B) this;
        }

        public B header(String name, String value) {
            getHeaders().put(name, value);
            return (B) this;
        }

        public B headers(Map<String, String> headers) {
            getHeaders().putAll(headers);
            return (B) this;
        }

        private Map<String, String> getHeaders() {
            if (headers == null) {
                headers = new LinkedHashMap<>();
            }
            return headers;
        }

        @Override
        protected T build(T target) {
            target.setUrl(url);
            target.setHeaders(headers);
            return super.build(target);
        }

        @Override
        public T build() {
            return build((T) new HttpSource());
        }
    }
}
