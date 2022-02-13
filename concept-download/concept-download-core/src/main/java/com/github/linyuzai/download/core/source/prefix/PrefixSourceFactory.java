package com.github.linyuzai.download.core.source.prefix;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.SourceFactory;

/**
 * 基于字符串前缀匹配的 {@link SourceFactory}。
 */
public abstract class PrefixSourceFactory implements SourceFactory {

    /**
     * 需要String类型并且前缀匹配的才能支持。
     *
     * @param source  需要下载的原始数据对象
     * @param context {@link DownloadContext}
     * @return 如果支持则返回 true
     */
    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof String && (matchPrefix((String) source) != null);
    }

    /**
     * 匹配前缀。
     *
     * @param source 需要下载的原始数据对象
     * @return 匹配到的前缀，未匹配到返回 null
     */
    protected String matchPrefix(String source) {
        String[] prefixes = getPrefixes();
        for (String prefix : prefixes) {
            if (source.startsWith(prefix)) {
                return prefix;
            }
        }
        return null;
    }

    /**
     * 获得内容，即去掉前缀之后的数据。
     *
     * @param source 需要下载的原始数据对象
     * @return 内容
     */
    protected String getContent(String source) {
        String prefix = matchPrefix(source);
        return source.substring(prefix.length());
    }

    /**
     * 获得支持的前缀。
     *
     * @return 支持的前缀
     */
    protected abstract String[] getPrefixes();
}
