package com.github.linyuzai.download.core.response;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.contenttype.ContentType;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.handler.AutomaticDownloadHandler;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.request.DownloadRequest;
import com.github.linyuzai.download.core.request.DownloadRequestProvider;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import com.github.linyuzai.download.core.writer.DownloadWriterAdapter;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

/**
 * 写响应处理器 / A handler to write response
 */
@AllArgsConstructor
public class WriteResponseHandler implements AutomaticDownloadHandler, DownloadContextInitializer {

    private DownloadWriterAdapter downloadWriterAdapter;

    private DownloadRequestProvider downloadRequestProvider;

    private DownloadResponseProvider downloadResponseProvider;

    /**
     * 将压缩后的对象写入输出流 / Writes the compressed object to the output stream
     * 设置inline或文件名 / Set inline or file name
     * 设置ContentType / Set ContentType
     * 设置ContentLength / Set ContentLength
     * 设置响应头 / Set response headers
     *
     * @param context 下载上下文 / Context of download
     * @throws IOException I/O exception
     */
    @Override
    public void doHandle(DownloadContext context) throws IOException {
        DownloadResponse response = context.get(DownloadResponse.class);
        Compression compression = context.get(Compression.class);
        boolean inline = context.getOptions().isInline();
        if (inline) {
            response.setInline();
        } else {
            String filename = context.getOptions().getFilename();
            if (filename == null || filename.isEmpty()) {
                response.setFilename(compression.getName());
            } else {
                response.setFilename(filename);
            }
        }
        String contentType = context.getOptions().getContentType();
        if (contentType == null || contentType.isEmpty()) {
            String compressionContentType = compression.getContentType();
            if (compressionContentType == null || compressionContentType.isEmpty()) {
                response.setContentType(ContentType.Application.OCTET_STREAM);
            } else {
                response.setContentType(compressionContentType);
            }
        } else {
            response.setContentType(contentType);
        }
        Long length = compression.getLength();
        response.setContentLength(length);
        Map<String, String> headers = context.getOptions().getHeaders();
        if (headers != null) {
            response.setHeaders(headers);
        }
        Range range = context.get(Range.class);
        DownloadWriter writer = downloadWriterAdapter.getWriter(compression, range, context);
        write(response.getOutputStream(), writer, range, compression.getParts());
    }

    protected void write(OutputStream os, DownloadWriter writer, Range range, Collection<Part> parts) throws IOException {
        for (Part part : parts) {
            InputStream inputStream = part.getInputStream();
            if (inputStream != null) {
                writer.write(part.getInputStream(), os, range, part.getCharset(), part.getLength());
            }
            write(os, writer, range, part.getChildren());
        }
    }

    /**
     * 初始化时，将请求和响应放到上下文中 / Put the request and response into context when initializing
     *
     * @param context 下载上下文 / Context of download
     */
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
