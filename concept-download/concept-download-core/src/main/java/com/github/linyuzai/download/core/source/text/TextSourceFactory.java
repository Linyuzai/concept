package com.github.linyuzai.download.core.source.text;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;

import java.nio.charset.Charset;

public class TextSourceFactory implements SourceFactory {

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof String;
    }

    @Override
    public Source create(Object source, DownloadContext context) {
        Charset charset = context.getOptions().getCharset();
        return new TextSource.Builder()
                .text((String) source)
                .name("text.txt")
                .charset(charset)
                .build();
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 100;
    }
}
