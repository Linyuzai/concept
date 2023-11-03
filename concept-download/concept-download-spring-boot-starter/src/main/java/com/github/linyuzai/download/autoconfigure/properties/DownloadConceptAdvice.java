package com.github.linyuzai.download.autoconfigure.properties;

import com.github.linyuzai.download.core.annotation.CompressCache;
import com.github.linyuzai.download.core.annotation.Download;
import com.github.linyuzai.download.core.annotation.SourceCache;
import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.concept.ValueContainer;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 下载功能的AOP拦截。
 * 对标注了注解 {@link Download} 的方法进行拦截。
 */
public class DownloadConceptAdvice extends DefaultPointcutAdvisor implements MethodInterceptor, BeanPostProcessor {

    /**
     * 下载处理的统一入口
     */
    @Getter
    @Setter
    private DownloadConcept downloadConcept;

    public DownloadConceptAdvice() {
        setPointcut(new AnnotationMatchingPointcut(null, Download.class, true));
        setAdvice(this);
        setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    /**
     * 根据全局配置
     * 注解 {@link Download} {@link SourceCache} {@link CompressCache}
     * 和返回值构建一个 {@link DownloadOptions}
     * 并调用 {@link DownloadConcept#download(DownloadOptions)} 处理下载数据。
     *
     * @param invocation {@link MethodInvocation}
     * @return null or {@link Mono<Void>}
     * @throws Throwable 方法调用或下载数据处理抛出异常
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Object[] arguments = invocation.getArguments();
        Object returnValue = unwrap(invocation.proceed());
        //DownloadOptions options = buildOptions(method, arguments, returnValue, configuration);
        return returnValue;
    }

    /**
     * 如果返回值是 {@link ValueContainer}，
     * 则调用 {@link ValueContainer#getValue()} 获得真实的方法返回值。
     *
     * @param value 方法返回值
     * @return 真实的方法返回值
     */
    private Object unwrap(Object value) {
        if (value instanceof ValueContainer) {
            return ((ValueContainer) value).getValue();
        } else {
            return value;
        }
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
     * @param method        切面方法
     * @param returnValue   方法返回值
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
     * @param download      注解 {@link Download}
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
     * @param download      注解 {@link Download}
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
     * @param cache         {@link SourceCache}
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
     * @param cache         {@link CompressCache}
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
