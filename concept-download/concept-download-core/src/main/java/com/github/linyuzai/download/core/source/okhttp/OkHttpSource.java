package com.github.linyuzai.download.core.source.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.http.HttpSource;
import lombok.*;
import okhttp3.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * 使用 {@link OkHttpClient} 处理 http 请求的 {@link Source}。
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OkHttpSource extends HttpSource {

    @NonNull
    @Setter(AccessLevel.PROTECTED)
    protected OkHttpClient client;

    @Override
    public InputStream loadRemote(DownloadContext context) throws IOException {
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        publisher.publish(new LoadOkHttpSourceEvent(context, this));
        Request.Builder rb = new Request.Builder();
        rb.url(url);
        if (headers != null) {
            rb.headers(Headers.of(headers));
        }
        Request request = rb.build();
        try (Response response = client.newCall(request).execute()) {
            int code = response.code();
            if (isResponseSuccess(code)) {
                ResponseBody body = response.body();
                if (body == null) {
                    throw new DownloadException("Body is null");
                }
                String contentType = getContentType();
                if (contentType == null || contentType.isEmpty()) {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        setContentType(mediaType.toString());
                    }
                }
                long l = body.contentLength();
                if (l != -1) {
                    length = l;
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
                throw new DownloadException("code: " + code + ", " + builder);
            }
        }
    }

    @Override
    public String getDescription() {
        return "OkHttpSource(" + url + ")";
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends OkHttpSource, B extends Builder<T, B>> extends HttpSource.Builder<T, B> {

        protected OkHttpClient client;

        public B client(OkHttpClient client) {
            this.client = client;
            return (B) this;
        }

        @Override
        protected T build(T target) {
            target.setClient(client);
            return super.build(target);
        }

        @Override
        public T build() {
            return build((T) new OkHttpSource());
        }
    }
}
