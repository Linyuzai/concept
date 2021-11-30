package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import com.github.linyuzai.download.core.original.OriginalSourceFactoryAdapter;
import com.github.linyuzai.download.core.request.DownloadRequest;
import com.github.linyuzai.download.core.request.DownloadRequestProvider;
import com.github.linyuzai.download.core.response.DownloadResponse;
import com.github.linyuzai.download.core.response.DownloadResponseProvider;
import com.github.linyuzai.download.core.writer.SourceWriterAdapter;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.IOException;

@AllArgsConstructor
public class InitializeContextInterceptor implements DownloadInterceptor {

    @NonNull
    private DownloadRequestProvider downloadRequestProvider;

    @NonNull
    private DownloadResponseProvider downloadResponseProvider;

    @NonNull
    private SourceWriterAdapter sourceWriterAdapter;

    @NonNull
    private final OriginalSourceFactoryAdapter originalSourceFactoryAdapter;

    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        DownloadRequest request = downloadRequestProvider.getRequest(context);
        context.set(DownloadRequest.class, request);

        DownloadResponse response = downloadResponseProvider.getResponse(context);
        context.set(DownloadResponse.class, response);

        context.set(SourceWriterAdapter.class, sourceWriterAdapter);
        context.set(OriginalSourceFactoryAdapter.class, originalSourceFactoryAdapter);

        chain.next(context);
    }

    @Override
    public int getOrder() {
        return ORDER_PROVIDE_REQUEST;
    }
}
