package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.http.HttpSourceFactory;
import com.github.linyuzai.download.core.source.prefix.PrefixSourceFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;

import java.nio.charset.Charset;

/**
 * 匹配前缀 'http://' 或 'https://' 并使用 {@link OkHttpClient} 的 {@link SourceFactory}。
 */
@Getter
@Setter
@AllArgsConstructor
public class OkHttpSourceFactory extends PrefixSourceFactory {

    private OkHttpClient client;

    public OkHttpSourceFactory() {
        this(new OkHttpClient());
    }

    @Override
    public Source create(Object source, DownloadContext context) {
        String url = (String) source;
        Charset charset = context.getOptions().getCharset();
        boolean cacheEnabled = context.getOptions().isSourceCacheEnabled();
        String cachePath = context.getOptions().getSourceCachePath();
        OkHttpSource build = new OkHttpSource.Builder<>()
                .client(client)
                .url(url)
                .asyncLoad(true)
                .charset(charset)
                .cacheEnabled(cacheEnabled)
                .cachePath(cachePath)
                .build();
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        publisher.publish(new OkHttpSourceCreatedEvent(context, build));
        return build;
    }

    @Override
    protected String[] getPrefixes() {
        return HttpSourceFactory.PREFIXES;
    }
}
