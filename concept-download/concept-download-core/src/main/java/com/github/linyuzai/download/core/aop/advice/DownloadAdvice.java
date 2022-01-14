package com.github.linyuzai.download.core.aop.advice;

import com.github.linyuzai.download.core.aop.annotation.CompressCache;
import com.github.linyuzai.download.core.aop.annotation.Download;
import com.github.linyuzai.download.core.aop.annotation.SourceCache;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.concept.ValueContainer;
import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.options.DownloadMethod;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.*;
import lombok.extern.apachecommons.CommonsLog;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 切面方法拦截器 / Interceptor of method on advice
 */
@NoArgsConstructor
@AllArgsConstructor
public class DownloadAdvice implements MethodInterceptor {

    @NonNull
    @Getter
    @Setter
    private DownloadConcept downloadConcept;

    /**
     * 拦截方法，处理下载请求 / Intercept method to process download request
     *
     * @param invocation {@link MethodInvocation}
     * @return null
     * @throws Throwable 异常 / exception
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Object[] arguments = invocation.getArguments();
        Object returnValue = unwrapContainer(invocation.proceed());
        return downloadConcept.download(configuration ->
                buildOptions(method, arguments, returnValue, configuration));
    }

    private Object unwrapContainer(Object value) {
        if (value instanceof ValueContainer) {
            return ((ValueContainer) value).getValue();
        } else {
            return value;
        }
    }

    /**
     * 构建下载参数 / Build download options
     * 注解的优先级高于配置 / Annotations take precedence over configuration
     * 如果返回值是下载参数对象则直接使用 / If the return value is a download parameter object, it is used directly
     * 如果返回值是null或者是重写接口 / If the return value is null or the interface to rewrite
     * 则Source使用注解上的参数 / the parameters on the annotation are used as source object
     * 否则返回值作为Source / Otherwise, use return value as source object
     * 如果返回值是重写接口则调用重写方法 / If the return value is a rewrite interface, the rewrite method will be called
     *
     * @param method        切面方法 / Method of advice
     * @param arguments     方法入参 / Parameters of method
     * @param returnValue   方法返回值 / Return value of method
     * @param configuration 下载的配置 / Configuration of download
     * @return 下载参数 / Download options
     */
    public DownloadOptions buildOptions(Method method,
                                        Object[] arguments,
                                        Object returnValue,
                                        DownloadConfiguration configuration) {

        Download download = method.getAnnotation(Download.class);
        SourceCache sourceCache = method.getAnnotation(SourceCache.class);
        CompressCache compressCache = method.getAnnotation(CompressCache.class);

        DownloadOptions.Builder builder = DownloadOptions.builder();

        DownloadMethod downloadMethod = DownloadMethod.builder()
                .method(method)
                .parameters(arguments)
                .returnValue(returnValue)
                .build();

        builder.downloadMethod(downloadMethod);

        if (returnValue instanceof DownloadOptions) {
            return (DownloadOptions) returnValue;
        }
        if (returnValue == null || returnValue instanceof DownloadOptions.Rewriter) {
            builder.source(download.source());
        } else {
            builder.source(returnValue);
        }

        builder.filename(download.filename())
                .inline(download.inline())
                .contentType(download.contentType())
                .compressFormat(buildCompressFormat(download, configuration))
                .forceCompress(download.forceCompress())
                .charset(buildCharset(download))
                .headers(buildHeaders(download, configuration))
                .logEnabled(configuration.getLog().isEnabled())
                .extra(download.extra());

        if (sourceCache == null) {
            DownloadConfiguration.CacheConfiguration cache =
                    configuration.getSource().getCache();
            builder.sourceCacheEnabled(cache.isEnabled())
                    .sourceCachePath(cache.getPath())
                    .sourceCacheDelete(cache.isDelete());
        } else {
            builder.sourceCacheEnabled(sourceCache.enabled())
                    .sourceCachePath(buildSourceCachePath(sourceCache, configuration))
                    .sourceCacheDelete(sourceCache.delete());
        }

        if (compressCache == null) {
            DownloadConfiguration.CacheConfiguration cache =
                    configuration.getCompress().getCache();
            builder.compressCacheEnabled(cache.isEnabled())
                    .compressCachePath(cache.getPath())
                    .compressCacheDelete(cache.isDelete());
        } else {
            builder.compressCacheEnabled(compressCache.enabled())
                    .compressCachePath(buildCompressPath(compressCache, configuration))
                    .compressCacheName(compressCache.name())
                    .compressCacheDelete(compressCache.delete());
        }

        DownloadOptions options = builder.build();

        if (returnValue instanceof DownloadOptions.Rewriter) {
            return ((DownloadOptions.Rewriter) returnValue).rewrite(options);
        } else {
            return options;
        }
    }

    /**
     * @param download      注解 / Annotation
     * @param configuration 下载配置 / Download configuration
     * @return 压缩格式 / Compression format
     */
    private String buildCompressFormat(Download download, DownloadConfiguration configuration) {
        String compressFormat = download.compressFormat();
        if (compressFormat.isEmpty()) {
            return configuration.getCompress().getFormat();
        } else {
            return compressFormat;
        }
    }

    /**
     * @param download 注解 / Annotation
     * @return 编码 / Charset
     */
    private Charset buildCharset(Download download) {
        String charset = download.charset();
        return charset.isEmpty() ? null : Charset.forName(charset);
    }

    /**
     * @param download      注解 / Annotation
     * @param configuration 下载配置 / Download configuration
     * @return 响应头 / Response headers
     */
    private Map<String, String> buildHeaders(Download download, DownloadConfiguration configuration) {
        Map<String, String> headerMap = new LinkedHashMap<>();
        Map<String, String> globalHeaders = configuration.getResponse().getHeaders();
        if (globalHeaders != null) {
            headerMap.putAll(globalHeaders);
        }
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
     * @param cache         注解 / Annotation
     * @param configuration 下载配置 / Download configuration
     * @return 下载源的缓存地址 / Cache path of source
     */
    private String buildSourceCachePath(SourceCache cache, DownloadConfiguration configuration) {
        if (cache.group().isEmpty()) {
            return configuration.getSource().getCache().getPath();
        } else {
            return new File(configuration.getSource().getCache().getPath(), cache.group()).getAbsolutePath();
        }
    }

    /**
     * @param cache         注解 / Annotation
     * @param configuration 下载配置 / Download configuration
     * @return 压缩的缓存地址 / Cache path of compression
     */
    private String buildCompressPath(CompressCache cache, DownloadConfiguration configuration) {
        if (cache.group().isEmpty()) {
            return configuration.getCompress().getCache().getPath();
        } else {
            return new File(configuration.getCompress().getCache().getPath(), cache.group()).getAbsolutePath();
        }
    }
}
