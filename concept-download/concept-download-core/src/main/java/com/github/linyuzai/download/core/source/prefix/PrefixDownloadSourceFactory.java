package com.github.linyuzai.download.core.source.prefix;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.DownloadSourceFactory;

/**
 * 基于字符串前缀匹配的加载器
 */
public abstract class PrefixDownloadSourceFactory implements DownloadSourceFactory {

    /**
     * String类型并且前缀匹配的才能加载
     *
     * @param source  需要下载的数据对象
     * @param context 下载上下文
     * @return 是否能够加载
     */
    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof String && (matchPrefix((String) source) != null);
    }

    /**
     * 匹配前缀
     *
     * @param source 需要下载的数据对象
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
     * 获得内容，及去掉前缀之后的数据
     *
     * @param source 需要下载的数据对象
     * @return 内容
     */
    protected String getContent(String source) {
        String prefix = matchPrefix(source);
        return source.substring(prefix.length());
    }

    /**
     * @return 支持的前缀
     */
    protected abstract String[] getPrefixes();
}
