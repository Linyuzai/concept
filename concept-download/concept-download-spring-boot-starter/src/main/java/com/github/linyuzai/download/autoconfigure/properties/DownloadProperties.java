package com.github.linyuzai.download.autoconfigure.properties;

import com.github.linyuzai.download.core.annotation.CompressCache;
import com.github.linyuzai.download.core.annotation.Download;
import com.github.linyuzai.download.core.annotation.SourceCache;
import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.source.Source;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 全局下载配置。
 */
@Data
@ConfigurationProperties(prefix = "concept.download")
public class DownloadProperties {

    private ResponseConfiguration response = new ResponseConfiguration();

    private SourceConfiguration source = new SourceConfiguration();

    private CompressConfiguration compress = new CompressConfiguration();

    private LoggerConfiguration logger = new LoggerConfiguration();

    @Data
    public static class ResponseConfiguration {

        /**
         * 额外的响应头
         */
        private Map<String, String> headers;
    }

    @Data
    public static class SourceConfiguration {

        private CacheConfiguration cache = new CacheConfiguration();
    }

    @Data
    public static class CompressConfiguration {

        /**
         * 压缩格式
         */
        private String format = CompressFormat.ZIP;

        private CacheConfiguration cache = new CacheConfiguration();
    }

    @Data
    public static class CacheConfiguration {

        /**
         * 是否启用缓存
         */
        private boolean enabled;

        /**
         * 缓存路径
         */
        private String path = Cacheable.PATH;

        /**
         * 下载结束后是否删除缓存。
         */
        private boolean delete;
    }

    @Data
    public static class LoggerConfiguration {

        /**
         * 是否启用日志
         */
        private boolean enabled;

        private StandardLoggerConfiguration standard = new StandardLoggerConfiguration();

        private TimeSpentLoggerConfiguration timeSpent = new TimeSpentLoggerConfiguration();

        private ProgressLoggerConfiguration progress = new ProgressLoggerConfiguration();
    }

    @Data
    public static class StandardLoggerConfiguration {

        /**
         * 是否启用标准日志
         */
        private boolean enabled = true;
    }

    @Data
    public static class TimeSpentLoggerConfiguration {

        /**
         * 是否启用事件计算日志
         */
        private boolean enabled = true;
    }

    @Data
    public static class ProgressLoggerConfiguration {

        /**
         * 是否启用进度计算日志
         */
        private boolean enabled = true;

        /**
         * 间隔，ms
         */
        private int duration = 1000;

        /**
         * 百分比计算
         */
        private boolean percentage;
    }

    public DownloadOptions toOptions(MethodParameter method, Object returnValue) {
        return buildOptions(method, returnValue, this);
    }

    /**
     * 构建下载参数，
     * 注解的优先级高于全局配置。
     * 如果返回值是 {@link DownloadOptions} 则直接使用；
     * 如果返回值是 null 或者是 {@link DownloadOptions.Rewriter}
     * 则使用 {@link Download#source()}，否则使用返回值。
     * 如果返回值是 {@link DownloadOptions.Rewriter}
     * 则会调用 {@link DownloadOptions.Rewriter#rewrite(DownloadOptions)}，
     * 回调给开发者重写 {@link DownloadOptions}。
     *
     * @param method      切面方法
     * @param returnValue 方法返回值
     * @return {@link DownloadOptions}
     */
    public static DownloadOptions buildOptions(MethodParameter method,
                                               Object returnValue,
                                               DownloadProperties properties) {
        //如果是 DownloadOptions 直接使用
        if (returnValue instanceof DownloadOptions) {
            return (DownloadOptions) returnValue;
        }
        Download download = Objects.requireNonNull(method.getMethodAnnotation(Download.class));
        SourceCache sourceCache = method.getMethodAnnotation(SourceCache.class);
        CompressCache compressCache = method.getMethodAnnotation(CompressCache.class);

        DownloadOptions.Builder builder = DownloadOptions.builder();

        builder.method(method.getMethod());
        builder.returnValue(returnValue);
        //如果为 null 或 Rewriter 则使用注解指定的数据，否则使用返回值
        if (returnValue == null || returnValue instanceof DownloadOptions.Rewriter) {
            builder.source(download.source());
        } else {
            builder.source(returnValue);
        }

        builder.filename(download.filename())
                .inline(download.inline())
                .contentType(download.contentType())
                .compressFormat(buildCompressFormat(download, properties))
                .forceCompress(download.forceCompress())
                .charset(buildCharset(download))
                .headers(buildHeaders(download, properties))
                .extra(download.extra());

        if (sourceCache == null) {
            DownloadProperties.CacheConfiguration cache =
                    properties.getSource().getCache();
            builder.sourceCacheEnabled(cache.isEnabled())
                    .sourceCachePath(cache.getPath())
                    .sourceCacheDelete(cache.isDelete());
        } else {
            builder.sourceCacheEnabled(sourceCache.enabled())
                    .sourceCachePath(buildSourceCachePath(sourceCache, properties))
                    .sourceCacheDelete(sourceCache.delete());
        }

        if (compressCache == null) {
            DownloadProperties.CacheConfiguration cache =
                    properties.getCompress().getCache();
            builder.compressCacheEnabled(cache.isEnabled())
                    .compressCachePath(cache.getPath())
                    .compressCacheDelete(cache.isDelete());
        } else {
            builder.compressCacheEnabled(compressCache.enabled())
                    .compressCachePath(buildCompressPath(compressCache, properties))
                    .compressCacheName(compressCache.name())
                    .compressCacheDelete(compressCache.delete());
        }

        DownloadOptions options = builder.build();

        if (returnValue instanceof DownloadOptions.Rewriter) {
            //回调重写接口
            return ((DownloadOptions.Rewriter) returnValue).rewrite(options);
        } else {
            return options;
        }
    }

    /**
     * 获得压缩格式。
     * 如果 {@link Download} 注解未指定压缩格式，
     * 默认为 ZIP。
     *
     * @param download 注解 {@link Download}
     * @return 压缩格式
     */
    private static String buildCompressFormat(Download download, DownloadProperties properties) {
        String compressFormat = download.compressFormat();
        if (StringUtils.hasText(compressFormat)) {
            return compressFormat;
        } else {
            return properties.getCompress().getFormat();
        }
    }

    /**
     * 获得编码。
     * 如果 {@link Download} 注解未指定编码，
     * 则编码为 null，否则使用指定的编码。
     *
     * @param download 注解 {@link Download}
     * @return 指定的编码或 null
     */
    private static Charset buildCharset(Download download) {
        String charset = download.charset();
        return StringUtils.hasText(charset) ? Charset.forName(charset) : null;
    }

    /**
     * 构建额外的响应头，每两个为一组，分别作为 name 和 value。
     *
     * @param download 注解 {@link Download}
     * @return 额外响应头的 {@link Map} 对象
     */
    private static Map<String, String> buildHeaders(Download download, DownloadProperties properties) {
        Map<String, String> headerMap = new LinkedHashMap<>();
        Map<String, String> globalHeaders = properties.getResponse().getHeaders();
        //全局响应头
        if (globalHeaders != null) {
            headerMap.putAll(globalHeaders);
        }
        //注解上指定的响应头
        String[] headers = download.headers();
        if (headers.length % 2 == 0) {
            for (int i = 0; i < headers.length; i += 2) {
                headerMap.put(headers[i], headers[i + 1]);
            }
            return headerMap;
        } else {
            throw new DownloadException("Headers params % 2 != 0");
        }
    }

    /**
     * 获得 {@link Source} 的缓存路径。
     *
     * @param cache {@link SourceCache}
     * @return {@link Source} 的缓存路径
     */
    private static String buildSourceCachePath(SourceCache cache, DownloadProperties properties) {
        String path = properties.getSource().getCache().getPath();
        if (cache.group().isEmpty()) {
            return path;
        } else {
            return new File(path, cache.group()).getAbsolutePath();
        }
    }

    /**
     * 获得 {@link Compression} 的缓存路径。
     *
     * @param cache {@link CompressCache}
     * @return {@link Compression} 的缓存路径
     */
    private static String buildCompressPath(CompressCache cache, DownloadProperties properties) {
        String path = properties.getCompress().getCache().getPath();
        if (cache.group().isEmpty()) {
            return path;
        } else {
            return new File(path, cache.group()).getAbsolutePath();
        }
    }
}
