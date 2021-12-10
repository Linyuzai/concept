package com.github.linyuzai.download.aop.advice;

import com.github.linyuzai.download.aop.annotation.Download;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
public class DownloadConceptAdvice implements MethodInterceptor {

    private DownloadConcept downloadConcept;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object returnValue = invocation.proceed();
        Download annotation = invocation.getMethod().getAnnotation(Download.class);
        Object[] arguments = invocation.getArguments();
        DownloadOptions options = buildOptions(annotation, arguments, returnValue);
        downloadConcept.download(options);
        return null;
    }

    public DownloadOptions buildOptions(Download annotation, Object[] arguments, Object returnValue) {
        DownloadOptions.Builder builder = DownloadOptions.builder();
        if (returnValue instanceof DownloadOptions) {
            return (DownloadOptions) returnValue;
        } else {
            if (returnValue == null) {
                builder.source(annotation.source());
            } else {
                builder.source(returnValue);
            }
        }

        return builder
                .sourceCacheEnabled(annotation.sourceCacheEnabled())
                .sourceCacheGroup(annotation.sourceCacheGroup())
                .deleteSourceCache(annotation.deleteSourceCache())
                .filename(annotation.filename())
                .contentType(annotation.contentType())
                .compressFormat(annotation.compressFormat())
                .compressOnSingle(annotation.compressOnSingle())
                //.compressKeepStruct(annotation.compressKeepStruct())
                .compressCacheEnabled(annotation.compressCacheEnabled())
                .compressCacheGroup(annotation.compressCacheGroup())
                .compressCacheName(annotation.compressCacheName())
                .deleteCompressCache(annotation.deleteCompressCache())
                .charset(toCharset(annotation.charset()))
                .headers(toHeaders(annotation.headers()))
                .extra(annotation.extra())
                .args(arguments)
                .build();
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
