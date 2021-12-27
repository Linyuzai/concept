package com.github.linyuzai.download.core.source.text;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;

import java.nio.charset.Charset;

/**
 * 支持文本类型的下载源工厂 / Factory support text
 */
public class TextSourceFactory implements SourceFactory {

    /**
     * 支持文本 / Array text
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 是否支持 / Is it supported
     */
    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof String;
    }

    /**
     * Use {@link TextSource}
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 下载源 / Source {@link TextSource}
     */
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
