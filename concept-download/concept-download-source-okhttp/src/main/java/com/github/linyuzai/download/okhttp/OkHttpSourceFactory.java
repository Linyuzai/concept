package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.prefix.PrefixSourceFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;

import java.nio.charset.Charset;

@Getter
@Setter
@AllArgsConstructor
public class OkHttpSourceFactory extends PrefixSourceFactory {

    public static final String[] PREFIXES = new String[]{"http://", "https://"};

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
        return new OkHttpSource.Builder()
                .client(client)
                .url(url)
                .asyncLoad(true)
                .charset(charset)
                .cacheEnabled(cacheEnabled)
                .cachePath(cachePath)
                .build();
    }

    @Override
    protected String[] getPrefixes() {
        return PREFIXES;
    }
}
