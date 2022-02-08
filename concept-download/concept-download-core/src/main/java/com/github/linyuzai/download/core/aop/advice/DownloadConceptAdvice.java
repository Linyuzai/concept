package com.github.linyuzai.download.core.aop.advice;

import com.github.linyuzai.download.core.aop.annotation.CompressCache;
import com.github.linyuzai.download.core.aop.annotation.Download;
import com.github.linyuzai.download.core.aop.annotation.SourceCache;
import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.concept.ValueContainer;
import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.options.DownloadMethod;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.source.Source;
import lombok.*;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import reactor.core.publisher.Mono;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 下载功能的AOP实现。
 * 对标注了注解 {@link Download} 的方法进行拦截，
 * 并委托 {@link DownloadConcept} 统一处理下载数据。
 */
public class DownloadConceptAdvice extends DefaultPointcutAdvisor implements MethodInterceptor, BeanPostProcessor {

    @Getter
    @Setter
    private DownloadConfiguration configuration;

    @Getter
    @Setter
    private DownloadConcept downloadConcept;

    public DownloadConceptAdvice() {
        setPointcut(new AnnotationMatchingPointcut(null, Download.class, true));
        setAdvice(this);
        setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    /**
     * 根据全局配置 {@link DownloadConfiguration}，
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
        DownloadOptions options = buildOptions(method, arguments, returnValue, configuration);
        return downloadConcept.download(options);
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
     * 注解的优先级高于 {@link DownloadConfiguration} 全局配置。
     * 如果返回值是 {@link DownloadOptions} 则直接使用；
     * 如果返回值是 null 或者是 {@link DownloadOptions.Rewriter}
     * 则使用 {@link Download#source()}，否则使用返回值。
     * 如果返回值是 {@link DownloadOptions.Rewriter}
     * 则会调用 {@link DownloadOptions.Rewriter#rewrite(DownloadOptions)}，
     * 回调给开发者重写 {@link DownloadOptions}。
     *
     * @param method        切面方法
     * @param arguments     方法入参
     * @param returnValue   方法返回值
     * @param configuration 全局配置 {@link DownloadConfiguration}
     * @return {@link DownloadOptions}
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
     * 获得压缩格式。
     * 如果 {@link Download} 注解未指定压缩格式，
     * 则使用全局配置 {@link DownloadConfiguration} 中的压缩格式，
     * 默认为 ZIP。
     *
     * @param download      注解 {@link Download}
     * @param configuration 全局配置 {@link DownloadConfiguration}
     * @return 最终的压缩格式
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
     * 获得编码。
     * 如果 {@link Download} 注解未指定编码，
     * 则编码为 null，否则使用指定的编码。
     *
     * @param download 注解 {@link Download}
     * @return 指定的编码或 null
     */
    private Charset buildCharset(Download download) {
        String charset = download.charset();
        return charset.isEmpty() ? null : Charset.forName(charset);
    }

    /**
     * 构建额外的响应头，每两个为一组，分别作为 name 和 value。
     *
     * @param download      注解 {@link Download}
     * @param configuration 全局配置 {@link DownloadConfiguration}
     * @return 额外响应头的 Map 对象
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
     * 获得 {@link Source} 的缓存路径。
     * <p>
     * Build the cache path of {@link Source}.
     *
     * @param cache         {@link SourceCache}
     * @param configuration {@link DownloadConfiguration}
     * @return 缓存路径
     * <p>
     * Cache path
     */
    private String buildSourceCachePath(SourceCache cache, DownloadConfiguration configuration) {
        String path = configuration.getSource().getCache().getPath();
        if (cache.group().isEmpty()) {
            return path;
        } else {
            return new File(path, cache.group()).getAbsolutePath();
        }
    }

    /**
     * 获得 {@link Compression} 的缓存路径。
     * <p>
     * Build the cache path of {@link Compression}.
     *
     * @param cache         {@link CompressCache}
     * @param configuration {@link DownloadConfiguration}
     * @return 缓存路径
     * <p>
     * Cache path
     */
    private String buildCompressPath(CompressCache cache, DownloadConfiguration configuration) {
        String path = configuration.getCompress().getCache().getPath();
        if (cache.group().isEmpty()) {
            return path;
        } else {
            return new File(path, cache.group()).getAbsolutePath();
        }
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (bean instanceof DownloadConcept) {
            this.downloadConcept = (DownloadConcept) bean;
        } else if (bean instanceof DownloadConfiguration) {
            this.configuration = (DownloadConfiguration) bean;
        }
        return bean;
    }
}
