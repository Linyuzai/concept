package com.github.linyuzai.download.core.handler.impl;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.web.*;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import com.github.linyuzai.download.core.writer.DownloadWriterAdapter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 写响应处理器 / A handler to write response
 */
@AllArgsConstructor
public class WriteResponseHandler implements DownloadHandler, DownloadContextInitializer {

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
     */
    @Override
    public Mono<Void> handle(DownloadContext context, DownloadHandlerChain chain) {
        Compression compression = context.get(Compression.class);
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        return downloadRequestProvider.getRequest(context).flatMap(request -> {
            Range range = request.getRange();
            DownloadWriter writer = downloadWriterAdapter.getWriter(compression, range, context);
            return downloadResponseProvider.getResponse(context)
                    .filter(response -> applyHeaders(response, compression, range, context))
                    .flatMap(response -> response.write(new Consumer<OutputStream>() {

                        @SneakyThrows
                        @Override
                        public void accept(OutputStream os) {
                            Collection<Part> parts = compression.getParts();
                            for (Part part : parts) {
                                writer.write(part.getInputStream(), os, range,
                                        part.getCharset(), part.getLength());
                            }
                            os.flush();
                        }
                    }));
        }).flatMap(it -> {
            publisher.publish(new ResponseWrittenEvent(context));
            return chain.next(context);
        });
    }

    public boolean applyHeaders(DownloadResponse response, Compression compression, Range range, DownloadContext context) {
        response.setBytesAcceptRanges();
        Long length = compression.getLength();
        if (range == null || length == null || length <= 0) {
            response.setContentLength(length);
        } else {
            if (range.isSupport()) {
                response.setStatusCode(206);
                if (range.hasStart() && range.hasEnd()) {
                    long l = range.getEnd() - range.getStart() + 1;
                    range.setLength(l);
                    response.setContentLength(l);
                    response.setContentRange(range.getStart(), range.getEnd(), length);
                } else if (range.hasStart()) {
                    long l = length - range.getStart();
                    range.setLength(l);
                    response.setContentLength(l);
                    response.setContentRange(range.getStart(), length - 1, length);
                } else if (range.hasEnd()) {
                    long l = range.getEnd();
                    range.setLength(l);
                    response.setContentLength(l);
                    response.setContentRange(length - range.getEnd(), length - 1, length);
                } else {
                    long l = length;
                    range.setLength(l);
                    response.setContentLength(l);
                    response.setContentRange(0, length - 1, length);
                }
            } else {
                response.setStatusCode(416);
                return false;
            }
        }

        boolean inline = context.getOptions().isInline();
        String filename = context.getOptions().getFilename();
        String filenameToUse;
        if (filename == null || filename.isEmpty()) {
            filenameToUse = compression.getName();
        } else {
            filenameToUse = filename;
        }
        if (inline) {
            response.setInline(filenameToUse);
        } else {
            response.setAttachment(filenameToUse);
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

        Map<String, String> headers = context.getOptions().getHeaders();
        if (headers != null) {
            response.setHeaders(headers);
        }

        return true;
    }

    /**
     * 初始化时，将请求和响应放到上下文中 / Put the request and response into context when initializing
     *
     * @param context 下载上下文 / Context of download
     */
    @Override
    public void initialize(DownloadContext context) {
        context.set(DownloadWriterAdapter.class, downloadWriterAdapter);
        context.set(DownloadRequestProvider.class, downloadRequestProvider);
        context.set(DownloadResponseProvider.class, downloadResponseProvider);
    }

    @Override
    public int getOrder() {
        return ORDER_WRITE_RESPONSE;
    }
}
