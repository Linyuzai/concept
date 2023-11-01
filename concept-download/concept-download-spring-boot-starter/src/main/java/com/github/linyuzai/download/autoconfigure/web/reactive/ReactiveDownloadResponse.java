package com.github.linyuzai.download.autoconfigure.web.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.web.DownloadResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 持有 {@link ServerHttpResponse} 的 {@link DownloadResponse}，用于 webflux。
 */
@Getter
public class ReactiveDownloadResponse implements DownloadResponse {

    private final ServerHttpResponse response;

    private OutputStream os;

    private Mono<Void> mono;

    public ReactiveDownloadResponse(ServerHttpResponse response) {
        this.response = response;
    }

    @Override
    public OutputStream getOutputStream(DownloadContext context) {
        if (os == null) {
            mono = response.writeWith(Flux.create(fluxSink -> {
                os = new FluxSinkOutputStream(fluxSink, response);
            }));
            context.set(Mono.class, mono);
        }
        return os;
    }

    @Override
    public void setStatusCode(int statusCode) {
        response.setStatusCode(HttpStatus.valueOf(statusCode));
    }

    @Override
    public void setContentType(String contentType) {
        if (contentType != null) {
            response.getHeaders().setContentType(MediaType.parseMediaType(contentType));
        }
    }

    @Override
    public void setContentLength(Long contentLength) {
        if (contentLength != null) {
            response.getHeaders().setContentLength(contentLength);
        }
    }

    @Override
    public void setHeader(String name, String value) {
        response.getHeaders().set(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        response.getHeaders().add(name, value);
    }

    @SneakyThrows
    @Override
    public void flush() {
        if (os != null) {
            os.flush();
        }
    }

    @AllArgsConstructor
    public static class FluxSinkOutputStream extends OutputStream {

        private FluxSink<DataBuffer> fluxSink;

        private ServerHttpResponse response;

        @Override
        public void write(byte[] b) throws IOException {
            writeSink(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            byte[] bytes = new byte[len];
            System.arraycopy(b, off, bytes, 0, len);
            writeSink(bytes);
        }

        @Override
        public void write(int b) throws IOException {
            writeSink((byte) b);
        }

        @Override
        public void flush() {
            fluxSink.complete();
        }

        public void writeSink(byte... bytes) {
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            fluxSink.next(buffer);
            //在这里可能有问题，但是目前没有没有需要释放的数据
            DataBufferUtils.release(buffer);
        }
    }
}
