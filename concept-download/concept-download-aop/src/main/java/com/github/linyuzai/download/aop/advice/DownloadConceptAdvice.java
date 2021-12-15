package com.github.linyuzai.download.aop.advice;

import com.github.linyuzai.download.aop.annotation.CompressCache;
import com.github.linyuzai.download.aop.annotation.Download;
import com.github.linyuzai.download.aop.annotation.SourceCache;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.options.DownloadMethod;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
public class DownloadConceptAdvice implements MethodInterceptor {

    private DownloadConcept downloadConcept;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object returnValue = invocation.proceed();
        Method method = invocation.getMethod();
        Object[] arguments = invocation.getArguments();
        downloadConcept.download(configuration ->
                buildOptions(method, arguments, returnValue, configuration));
        return null;
    }

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
                .contentType(buildContentType(download, configuration))
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

    private String buildContentType(Download download, DownloadConfiguration configuration) {
        String contentType = download.contentType();
        if (contentType.isEmpty()) {
            return configuration.getResponse().getContentType();
        } else {
            return contentType;
        }
    }

    private String buildCompressFormat(Download download, DownloadConfiguration configuration) {
        String compressFormat = download.compressFormat();
        if (compressFormat.isEmpty()) {
            return configuration.getCompress().getFormat();
        } else {
            return compressFormat;
        }
    }

    private Charset buildCharset(Download download) {
        String charset = download.charset();
        return charset.isEmpty() ? null : Charset.forName(charset);
    }

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

    private String buildSourceCachePath(SourceCache cache, DownloadConfiguration configuration) {
        if (cache.group().isEmpty()) {
            return configuration.getSource().getCache().getPath();
        } else {
            return new File(configuration.getSource().getCache().getPath(), cache.group()).getAbsolutePath();
        }
    }

    private String buildCompressPath(CompressCache cache, DownloadConfiguration configuration) {
        if (cache.group().isEmpty()) {
            return configuration.getCompress().getCache().getPath();
        } else {
            return new File(configuration.getCompress().getCache().getPath(), cache.group()).getAbsolutePath();
        }
    }
}
