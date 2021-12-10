package com.github.linyuzai.download.core.response;

import com.github.linyuzai.download.core.contenttype.ContentType;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.request.DownloadRequest;
import com.github.linyuzai.download.core.request.DownloadRequestProvider;
import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import com.github.linyuzai.download.core.writer.DownloadWriterAdapter;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
public class WriteResponseInterceptor implements DownloadInterceptor, DownloadContextInitializer {

    private DownloadWriterAdapter downloadWriterAdapter;

    private DownloadRequestProvider downloadRequestProvider;

    private DownloadResponseProvider downloadResponseProvider;

    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        DownloadResponse response = context.get(DownloadResponse.class);
        Downloadable downloadable = context.get(Downloadable.class);
        String filename = context.getOptions().getFilename();
        if (filename == null || filename.isEmpty()) {
            response.setFilename(downloadable.getName());
        } else {
            response.setFilename(filename);
        }
        String contentType = context.getOptions().getContentType();
        if (contentType == null || contentType.isEmpty()) {
            response.setContentType(ContentType.OCTET_STREAM);
        } else {
            response.setContentType(contentType);
        }
        Map<String, String> headers = context.getOptions().getHeaders();
        if (headers != null) {
            response.setHeaders(headers);
        }
        Range range = context.get(Range.class);
        DownloadWriter writer = downloadWriterAdapter.getWriter(downloadable, range, context);
        downloadable.write(response.getOutputStream(), range, writer);
        chain.next(context);
    }

    @Override
    public void initialize(DownloadContext context) {
        context.set(DownloadWriterAdapter.class, downloadWriterAdapter);
        DownloadRequest request = downloadRequestProvider.getRequest(context);
        context.set(DownloadRequest.class, request);
        DownloadResponse response = downloadResponseProvider.getResponse(context);
        context.set(DownloadResponse.class, response);
    }

    @Override
    public int getOrder() {
        return ORDER_WRITE_RESPONSE;
    }
}
