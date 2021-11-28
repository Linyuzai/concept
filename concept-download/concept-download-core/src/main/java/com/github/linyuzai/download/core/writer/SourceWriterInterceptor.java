package com.github.linyuzai.download.core.writer;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.IOException;

@AllArgsConstructor
public class SourceWriterInterceptor implements DownloadInterceptor {

    @NonNull
    private SourceWriter writer;

    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        context.set(SourceWriter.class, writer);
        chain.next(context);
    }

    @Override
    public int getOrder() {
        return ORDER_SOURCE_WRITER;
    }
}
