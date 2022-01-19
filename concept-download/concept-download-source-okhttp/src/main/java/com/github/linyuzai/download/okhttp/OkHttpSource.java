package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.source.http.HttpSource;
import lombok.*;
import okhttp3.*;
import reactor.core.publisher.Mono;

import java.io.InputStream;

/**
 * 使用OkHttp加载资源 / Use OkHttp to load source
 */
@SuppressWarnings("all")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OkHttpSource extends HttpSource {

    @Setter
    @NonNull
    protected OkHttpClient client;

    @SneakyThrows
    @Override
    public Mono<InputStream> loadRemote(DownloadContext context) {
        Request.Builder rb = new Request.Builder();
        rb.url(url);
        if (headers != null) {
            rb.headers(Headers.of(headers));
        }
        Request request = rb.build();
        Response response = client.newCall(request).execute();
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
            DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
            publisher.publish(new OkHttpSourceLoadedEvent(context, this));
            return Mono.just(body.byteStream());
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
            throw new DownloadException("code: " + code + ", " + builder.toString());
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
