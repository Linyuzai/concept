package com.github.linyuzai.download.aop.advice;

import com.github.linyuzai.download.aop.annotation.CompressCache;
import com.github.linyuzai.download.aop.annotation.Download;
import com.github.linyuzai.download.aop.annotation.SourceCache;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

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
        Download download = method.getAnnotation(Download.class);
        SourceCache sourceCache = method.getAnnotation(SourceCache.class);
        CompressCache compressCache = method.getAnnotation(CompressCache.class);
        Object[] arguments = invocation.getArguments();
        DownloadOptions options = buildOptions(download, sourceCache, compressCache, arguments, returnValue);
        downloadConcept.download(options);
        return null;
    }

    public DownloadOptions buildOptions(Download download,
                                        SourceCache sourceCache,
                                        CompressCache compressCache,
                                        Object[] arguments,
                                        Object returnValue) {

        DownloadOptions.Builder builder = DownloadOptions.builder();
        if (returnValue instanceof DownloadOptions) {
            return (DownloadOptions) returnValue;
        } else {
            if (returnValue == null) {
                builder.source(download.source());
            } else {
                builder.source(returnValue);
            }
        }

        builder.args(arguments);

        builder.filename(download.filename())
                .contentType(download.contentType())
                .compressFormat(download.compressFormat())
                .forceCompress(download.forceCompress())
                .charset(toCharset(download.charset()))
                .headers(toHeaders(download.headers()))
                .extra(download.extra());

        if (sourceCache == null) {

        } else {
            builder.sourceCacheEnabled(sourceCache.enabled())
                    .sourceCacheGroup(sourceCache.group())
                    .sourceCacheDelete(sourceCache.delete());
        }

        if (compressCache == null) {

        } else {
            builder.compressCacheEnabled(compressCache.enabled())
                    .compressCacheGroup(compressCache.group())
                    .compressCacheName(compressCache.name())
                    .compressCacheDelete(compressCache.delete());
        }

        return builder.build();
    }

    private Charset toCharset(String charset) {
        return charset.isEmpty() ? null : Charset.forName(charset);
    }

    private Map<String, String> toHeaders(String[] headers) {
        if (headers.length % 2 == 0) {
            Map<String, String> headerMap = new LinkedHashMap<>();
            for (int i = 0; i < headers.length; i += 2) {
                headerMap.put(headers[i], headers[i + 1]);
            }
            return headerMap;
        } else {
            throw new DownloadException("Headers params % 2 != 0");
        }
    }
}
