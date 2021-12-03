package com.github.linyuzai.download.aop.advice;

import com.github.linyuzai.download.aop.annotation.Download;
import com.github.linyuzai.download.core.concept.DownloadConcept;
import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.nio.charset.Charset;

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
                builder.original(annotation.original());
            } else {
                builder.original(returnValue);
            }
        }

        return builder
                .originalCacheEnabled(annotation.originalCacheEnabled())
                .originalCacheGroup(annotation.originalCacheGroup())
                .filename(annotation.filename())
                .contentType(annotation.contentType())
                .compressEnabled(annotation.compressEnabled())
                .compressFormat(annotation.compressFormat())
                .skipCompressOnSingle(annotation.skipCompressOnSingle())
                //.compressKeepStruct(annotation.compressKeepStruct())
                .compressCacheEnabled(annotation.compressCacheEnabled())
                .compressCacheGroup(annotation.compressCacheGroup())
                .compressCacheName(annotation.compressCacheName())
                .deleteCompressCache(annotation.deleteCompressCache())
                .charset(annotation.charset().isEmpty() ? null : Charset.forName(annotation.charset()))
                .args(arguments)
                .extra(annotation.extra())
                .build();
    }
}
