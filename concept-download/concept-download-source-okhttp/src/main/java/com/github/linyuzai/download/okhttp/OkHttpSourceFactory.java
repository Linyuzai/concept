package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.http.HttpSourceFactory;
import com.github.linyuzai.download.core.source.prefix.PrefixSourceFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import okhttp3.OkHttpClient;

import java.nio.charset.Charset;

/**
 * 匹配http前缀，使用OkHttp加载的下载源的工厂 / The factory of the source witch match the HTTP prefix and use okhttp to load
 */
@CommonsLog
@Getter
@Setter
@AllArgsConstructor
public class OkHttpSourceFactory extends PrefixSourceFactory {

    private OkHttpClient client;

    public OkHttpSourceFactory() {
        this(new OkHttpClient());
    }

    /**
     * Use {@link OkHttpSource}
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 下载源 / Source {@link OkHttpSource}
     */
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
        context.log("[Create source] " + build);
        return build;
    }

    @Override
    protected String[] getPrefixes() {
        return HttpSourceFactory.PREFIXES;
    }
}
