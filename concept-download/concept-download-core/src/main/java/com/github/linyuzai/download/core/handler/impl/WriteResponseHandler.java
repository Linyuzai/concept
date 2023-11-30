package com.github.linyuzai.download.core.handler.impl;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.concept.Resource;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.event.DownloadLifecycleListener;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.web.*;
import com.github.linyuzai.download.core.write.DownloadWriter;
import com.github.linyuzai.download.core.write.DownloadWriterAdapter;
import com.github.linyuzai.download.core.write.Progress;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 将 {@link Compression} 写入到 {@link DownloadResponse}。
 */
@Getter
@RequiredArgsConstructor
public class WriteResponseHandler implements DownloadHandler, DownloadLifecycleListener {

    /**
     * {@link DownloadWriter} 适配器
     */
    private final DownloadWriterAdapter downloadWriterAdapter;

    @Override
    public boolean support(DownloadContext context) {
        DownloadOptions options = context.get(DownloadOptions.class);
        return options.getAsyncConsumer() == null;
    }

    /**
     * 写 {@link DownloadResponse}。
     * 处理 {@link DownloadRequest} 中的 {@link Range}，
     * 设置 {@link DownloadResponse} 的响应头，
     * 将 {@link Compression} 写入到 {@link DownloadResponse} 中
     * 并发布 {@link ResponseWritingProgressEvent} 事件，
     * 最后发布 {@link AfterResponseWrittenEvent} 事件。
     *
     * @param context {@link DownloadContext}
     */
    @SneakyThrows
    @Override
    public Object handle(DownloadContext context, DownloadHandlerChain chain) {
        Compression compression = context.get(Compression.class);
        DownloadOptions options = context.get(DownloadOptions.class);
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        //获得Request
        DownloadRequest request = options.getRequest();
        //获得Response
        DownloadResponse response = options.getResponse();
        //获得Range
        Range range = request.getRange();
        context.set(Range.class, range);
        //设置响应头
        if (applyHeaders(response, compression, range, context)) {
            //写数据
            DownloadWriter writer = downloadWriterAdapter.getWriter(compression, context);
            return response.write(new Consumer<OutputStream>() {

                @SneakyThrows
                @Override
                public void accept(OutputStream os) {
                    Collection<Part> parts = compression.getParts();
                    Progress progress = new Progress(compression.getLength());
                    for (Part part : parts) {
                        InputStream is = part.getInputStream();
                        writer.write(is, os, range, part.getCharset(), part.getLength(), (current, increase) -> {
                            //更新并发布写入进度
                            progress.update(increase);
                            publisher.publish(new ResponseWritingProgressEvent(context, progress.freeze()));
                        });
                    }
                    os.flush();
                }
            }, () -> chain.next(context), () -> publisher.publish(new AfterResponseWrittenEvent(context)));
        } else {
            return chain.next(context);
        }
    }

    /**
     * 设置响应头。
     * 处理 {@link Range}，
     * 设置 inline 或 attachment，
     * 设置 Content-Type，
     * 设置自定义的响应头。
     *
     * @param response    {@link DownloadResponse}
     * @param resource {@link Compression}
     * @param range       {@link Range}
     * @param context     {@link DownloadContext}
     * @return 是否继续处理
     */
    public boolean applyHeaders(DownloadResponse response, Resource resource, Range range, DownloadContext context) {
        //Range处理
        response.setBytesAcceptRanges();
        Long length = resource.getLength();
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

        DownloadOptions options = context.get(DownloadOptions.class);

        //inline 或 attachment
        boolean inline = options.isInline();
        String filename = options.getFilename();
        String filenameToUse;
        if (filename == null || filename.isEmpty()) {
            filenameToUse = resource.getName();
        } else {
            filenameToUse = filename;
        }
        if (inline) {
            response.setInline(filenameToUse);
        } else {
            response.setAttachment(filenameToUse);
        }

        //ContentType
        String contentType = options.getContentType();
        if (contentType == null || contentType.isEmpty()) {
            String compressionContentType = resource.getContentType();
            if (compressionContentType == null || compressionContentType.isEmpty()) {
                response.setContentType(ContentType.Application.OCTET_STREAM);
            } else {
                response.setContentType(compressionContentType);
            }
        } else {
            response.setContentType(contentType);
        }

        //Headers
        Map<String, String> headers = options.getHeaders();
        if (headers != null) {
            response.setHeaders(headers);
        }

        return true;
    }

    /**
     * 初始化时，将 {@link DownloadWriterAdapter} 设置到 {@link DownloadContext} 中；
     *
     * @param context {@link DownloadContext}
     */
    @Override
    public void onStart(DownloadContext context) {
        context.set(DownloadWriterAdapter.class, downloadWriterAdapter);
    }
}
