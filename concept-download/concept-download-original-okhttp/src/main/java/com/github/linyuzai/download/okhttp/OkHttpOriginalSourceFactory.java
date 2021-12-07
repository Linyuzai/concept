package com.github.linyuzai.download.okhttp;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.original.prefix.PrefixOriginalSourceFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;

import java.nio.charset.Charset;

@Getter
@Setter
@AllArgsConstructor
public class OkHttpOriginalSourceFactory extends PrefixOriginalSourceFactory {

    public static final String[] PREFIXES = new String[]{"http://", "https://"};

    private OkHttpClient client;

    public OkHttpOriginalSourceFactory() {
        this(new OkHttpClient());
    }

    @Override
    public OriginalSource create(Object source, DownloadContext context) {
        String path = getContent((String) source);
        Charset charset = context.getOptions().getCharset();
        return null;
    }

    @Override
    protected String[] getPrefixes() {
        return PREFIXES;
    }
}
