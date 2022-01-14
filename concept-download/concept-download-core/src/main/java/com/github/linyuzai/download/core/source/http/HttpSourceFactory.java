package com.github.linyuzai.download.core.source.http;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.prefix.PrefixSourceFactory;
import lombok.extern.apachecommons.CommonsLog;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

@CommonsLog
public class HttpSourceFactory extends PrefixSourceFactory {

    public static final String[] PREFIXES = new String[]{"http://", "https://"};

    @Override
    public Mono<Source> create(Object source, DownloadContext context) {
        String url = (String) source;
        Charset charset = context.getOptions().getCharset();
        boolean cacheEnabled = context.getOptions().isSourceCacheEnabled();
        String cachePath = context.getOptions().getSourceCachePath();
        HttpSource build = new HttpSource.Builder<>()
                .url(url)
                .asyncLoad(true)
                .charset(charset)
                .cacheEnabled(cacheEnabled)
                .cachePath(cachePath)
                .build();
        context.log("[Create source] " + build);
        return Mono.just(build);
    }

    @Override
    protected String[] getPrefixes() {
        return PREFIXES;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 1000;
    }
}
