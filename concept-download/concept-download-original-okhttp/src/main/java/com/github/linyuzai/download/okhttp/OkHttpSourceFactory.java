package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.cache.DownloadCacheLocation;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.prefix.PrefixSourceFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;

import java.io.File;
import java.nio.charset.Charset;

@Getter
@Setter
@AllArgsConstructor
public class OkHttpSourceFactory extends PrefixSourceFactory {

    public static final String[] PREFIXES = new String[]{"http://", "https://"};

    private OkHttpClient client;

    private DownloadCacheLocation cacheLocation;

    public OkHttpSourceFactory(DownloadCacheLocation cacheLocation) {
        this(new OkHttpClient(), cacheLocation);
    }

    @Override
    public Source create(Object source, DownloadContext context) {
        String url = (String) source;
        Charset charset = context.getOptions().getCharset();
        boolean cacheEnabled = context.getOptions().isSourceCacheEnabled();
        String path = cacheLocation.getPath();
        String group = context.getOptions().getSourceCacheGroup();
        File cacheDir = (group == null || group.isEmpty()) ? new File(path) : new File(path, group);
        return new OkHttpSource.Builder()
                .client(client)
                .url(url)
                .asyncLoad(true)
                .charset(charset)
                .cacheEnabled(cacheEnabled)
                .cachePath(cacheDir.getAbsolutePath())
                .build();
    }

    @Override
    protected String[] getPrefixes() {
        return PREFIXES;
    }
}
