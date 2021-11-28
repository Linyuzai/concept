package com.github.linyuzai.download.core.response;

import com.github.linyuzai.download.core.compress.CompressedSource;
import com.github.linyuzai.download.core.contenttype.ContentType;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptor;
import com.github.linyuzai.download.core.interceptor.DownloadInterceptorChain;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.writer.SourceWriter;

import java.io.IOException;

public class WriteResponseInterceptor implements DownloadInterceptor {

    @Override
    public void intercept(DownloadContext context, DownloadInterceptorChain chain) throws IOException {
        DownloadResponse response = context.get(DownloadResponse.class);
        CompressedSource source = context.get(CompressedSource.class);
        String filename = context.getOptions().getFilename();
        if (filename == null || filename.isEmpty()) {
            response.setFilename(source.getName());
        } else {
            response.setFilename(filename);
        }
        String contentType = context.getOptions().getContentType();
        if (contentType == null || contentType.isEmpty()) {
            response.setContentType(ContentType.OCTET_STREAM);
        } else {
            response.setContentType(contentType);
        }
        Range range = context.get(Range.class);
        SourceWriter writer = context.get(SourceWriter.class);
        source.write(response.getOutputStream(), range, writer);
        chain.next(context);
    }

    @Override
    public int getOrder() {
        return ORDER_WRITE_RESPONSE;
    }
}
