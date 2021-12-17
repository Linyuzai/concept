package com.github.linyuzai.download.core.source.prefix;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.SourceFactory;

/**
 * 基于字符串前缀匹配的工厂 / Factory based on string prefix matching
 */
public abstract class PrefixSourceFactory implements SourceFactory {

    /**
     * 需要String类型并且前缀匹配的才能支持 / String type and prefix matching are required to support
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 是否支持 / If supported
     */
    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof String && (matchPrefix((String) source) != null);
    }

    /**
     * 匹配前缀 / Match prefix
     *
     * @param source 需要下载的数据对象的字符串表示 / String representation of the data object to be downloaded
     * @return 匹配到的前缀，为匹配到返回null
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
     * 获得内容，及去掉前缀之后的数据 / Get the content and the data after removing the prefix
     *
     * @param source 需要下载的数据对象的字符串表示 / String representation of the data object to be downloaded
     * @return 内容 / Content
     */
    protected String getContent(String source) {
        String prefix = matchPrefix(source);
        return source.substring(prefix.length());
    }

    /**
     * @return 支持的前缀 / Supported prefixes
     */
    protected abstract String[] getPrefixes();
}
