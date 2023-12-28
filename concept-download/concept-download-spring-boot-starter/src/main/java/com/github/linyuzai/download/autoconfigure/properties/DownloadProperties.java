package com.github.linyuzai.download.autoconfigure.properties;

import com.github.linyuzai.download.core.annotation.CompressCache;
import com.github.linyuzai.download.core.annotation.Download;
import com.github.linyuzai.download.core.annotation.SourceCache;
import com.github.linyuzai.download.core.cache.Cacheable;
import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.options.ConfigurableDownloadOptions;
import com.github.linyuzai.download.core.options.DefaultDownloadOptions;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.utils.DownloadUtils;
import com.github.linyuzai.download.core.web.DownloadRequest;
import com.github.linyuzai.download.core.web.DownloadResponse;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 全局下载配置。
 */
@Data
@ConfigurationProperties(prefix = "concept.download")
public class DownloadProperties {

    private ResponseProperties response = new ResponseProperties();

    private SourceProperties source = new SourceProperties();

    private CompressProperties compress = new CompressProperties();

    private LoggerProperties logger = new LoggerProperties();

    @Data
    public static class ResponseProperties {

        /**
         * 额外的响应头
         */
        private Map<String, String> headers;
    }

    @Data
    public static class SourceProperties {

        private CacheProperties cache = new CacheProperties();
    }

    @Data
    public static class CompressProperties {

        /**
         * 压缩格式
         */
        private String format = CompressFormat.ZIP;

        private String password;

        private CacheProperties cache = new CacheProperties();
    }

    @Data
    public static class CacheProperties {

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
    public static class LoggerProperties {

        /**
         * 是否启用日志
         */
        private boolean enabled = true;

        private ErrorProperties error = new ErrorProperties();

        private StandardProperties standard = new StandardProperties();

        private TimeSpentProperties timeSpent = new TimeSpentProperties();

        private ProgressProperties progress = new ProgressProperties();

        @Data
        public static class ErrorProperties {

            /**
             * 是否启用异常日志
             */
            private boolean enabled = true;
        }

        @Data
        public static class StandardProperties {

            /**
             * 是否启用标准日志
             */
            private boolean enabled = true;
        }

        @Data
        public static class TimeSpentProperties {

            /**
             * 是否启用事件计算日志
             */
            private boolean enabled = true;
        }

        @Data
        public static class ProgressProperties {

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
    }

    public DownloadOptions toOptions(MethodParameter method, Object returnValue,
                                     DownloadRequest request, DownloadResponse response,
                                     StringValueResolver resolver) {
        return buildOptions(method, returnValue, request, response, this, resolver);
    }

    /**
     * 构建下载参数，
     * 注解的优先级高于全局配置。
     * 如果返回值是 {@link DownloadOptions} 则直接使用；
     * 如果返回值是 null 或者是 {@link DownloadOptions.Configurer}
     * 则使用 {@link Download#source()}，否则使用返回值。
     * 如果返回值是 {@link DownloadOptions.Configurer}
     * 则会调用 {@link DownloadOptions.Configurer#configure(ConfigurableDownloadOptions)} ，
     * 回调给开发者重写 {@link DownloadOptions}。
     *
     * @param method      切面方法
     * @param returnValue 方法返回值
     * @return {@link DownloadOptions}
     */
    public static DownloadOptions buildOptions(MethodParameter method,
                                               Object returnValue,
                                               DownloadRequest request,
                                               DownloadResponse response,
                                               DownloadProperties properties,
                                               StringValueResolver resolver) {
        //如果是 DownloadOptions 直接使用
        if (returnValue instanceof DownloadOptions) {
            return (DownloadOptions) returnValue;
        }
        Download download = Objects.requireNonNull(method.getMethodAnnotation(Download.class));
        SourceCache sourceCache = method.getMethodAnnotation(SourceCache.class);
        CompressCache compressCache = method.getMethodAnnotation(CompressCache.class);

        DefaultDownloadOptions options = new DefaultDownloadOptions();
        options.setRequest(request);
        options.setResponse(response);
        options.setMethod(method.getMethod());
        options.setReturnValue(returnValue);
        //如果为 null 或 Configurer 则使用注解指定的数据，否则使用返回值
        if (returnValue == null || returnValue instanceof DownloadOptions.Configurer) {
            options.setSource(Arrays.stream(download.source())
                    .map(resolver::resolveStringValue)
                    .toArray(String[]::new));
        } else {
            options.setSource(returnValue);
        }

        options.setFilename(resolver.resolveStringValue(download.filename()));
        options.setInline(download.inline());
        options.setContentType(resolver.resolveStringValue(download.contentType()));
        options.setCompressFormat(buildCompressFormat(download, properties, resolver));
        options.setCompressPassword(buildCompressPassword(download, properties, resolver));
        options.setForceCompress(download.forceCompress());
        options.setCharset(buildCharset(download, resolver));
        options.setHeaders(buildHeaders(download, properties, resolver));
        options.setExtra(resolver.resolveStringValue(download.extra()));

        if (sourceCache == null) {
            CacheProperties cache = properties.getSource().getCache();
            options.setSourceCacheEnabled(cache.isEnabled());
            options.setSourceCachePath(cache.getPath());
            options.setSourceCacheDelete(cache.isDelete());
        } else {
            options.setSourceCacheEnabled(sourceCache.enabled());
            options.setSourceCachePath(buildSourceCachePath(sourceCache, properties, resolver));
            options.setSourceCacheDelete(sourceCache.delete());
        }

        if (compressCache == null) {
            CacheProperties cache = properties.getCompress().getCache();
            options.setCompressCacheEnabled(cache.isEnabled());
            options.setCompressCachePath(cache.getPath());
            options.setCompressCacheDelete(cache.isDelete());
        } else {
            options.setCompressCacheEnabled(compressCache.enabled());
            options.setCompressCachePath(buildCompressPath(compressCache, properties, resolver));
            options.setCompressCacheName(compressCache.name());
            options.setCompressCacheDelete(compressCache.delete());
        }

        if (returnValue instanceof DownloadOptions.Configurer) {
            //回调重写接口
            ((DownloadOptions.Configurer) returnValue).configure(options);
        }
        if (DownloadUtils.isEmpty(options.getSource())) {
            throw new IllegalArgumentException("Nothing to download");
        }
        return options;
    }

    /**
     * 获得压缩格式。
     * 如果 {@link Download} 注解未指定压缩格式，
     * 默认为 ZIP。
     *
     * @param download 注解 {@link Download}
     * @return 压缩格式
     */
    private static String buildCompressFormat(Download download,
                                              DownloadProperties properties,
                                              StringValueResolver resolver) {
        String compressFormat = download.compressFormat();
        if (StringUtils.hasText(compressFormat)) {
            return resolver.resolveStringValue(compressFormat);
        } else {
            return properties.getCompress().getFormat();
        }
    }

    private static String buildCompressPassword(Download download,
                                                DownloadProperties properties,
                                                StringValueResolver resolver) {
        String compressPassword = download.compressPassword();
        if (StringUtils.hasText(compressPassword)) {
            return resolver.resolveStringValue(compressPassword);
        } else {
            return properties.getCompress().getPassword();
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
    private static Charset buildCharset(Download download, StringValueResolver resolver) {
        String charset = download.charset();
        if (StringUtils.hasText(charset)) {
            String resolved = resolver.resolveStringValue(charset);
            if (StringUtils.hasText(resolved)) {
                return Charset.forName(resolved);
            }
        }
        return null;
    }

    /**
     * 构建额外的响应头，每两个为一组，分别作为 name 和 value。
     *
     * @param download 注解 {@link Download}
     * @return 额外响应头的 {@link Map} 对象
     */
    private static Map<String, String> buildHeaders(Download download,
                                                    DownloadProperties properties,
                                                    StringValueResolver resolver) {
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
                String name = resolver.resolveStringValue(headers[i]);
                String value = resolver.resolveStringValue(headers[i + 1]);
                headerMap.put(name, value);
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
    private static String buildSourceCachePath(SourceCache cache,
                                               DownloadProperties properties,
                                               StringValueResolver resolver) {
        String path = properties.getSource().getCache().getPath();
        if (cache.group().isEmpty()) {
            return path;
        } else {
            String resolved = resolver.resolveStringValue(cache.group());
            if (StringUtils.hasText(resolved)) {
                return new File(path, resolved).getAbsolutePath();
            } else {
                return path;
            }
        }
    }

    /**
     * 获得 {@link Compression} 的缓存路径。
     *
     * @param cache {@link CompressCache}
     * @return {@link Compression} 的缓存路径
     */
    private static String buildCompressPath(CompressCache cache,
                                            DownloadProperties properties,
                                            StringValueResolver resolver) {
        String path = properties.getCompress().getCache().getPath();
        if (cache.group().isEmpty()) {
            return path;
        } else {
            String resolved = resolver.resolveStringValue(cache.group());
            if (StringUtils.hasText(resolved)) {
                return new File(path, resolved).getAbsolutePath();
            } else {
                return path;
            }
        }
    }
}
