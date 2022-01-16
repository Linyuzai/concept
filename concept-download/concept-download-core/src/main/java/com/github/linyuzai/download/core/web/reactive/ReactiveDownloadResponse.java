package com.github.linyuzai.download.core.web.reactive;

import com.github.linyuzai.download.core.web.DownloadResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * ServerHttpResponse实现 / implementations by ServerHttpResponse
 */
@Getter
public class ReactiveDownloadResponse implements DownloadResponse {

    private final ServerHttpResponse response;

    public ReactiveDownloadResponse(ServerHttpResponse response) {
        this.response = response;
    }

    @Override
    public Mono<DownloadResponse> write(Consumer<OutputStream> consumer) {
        return response.writeWith(Flux.create(fluxSink -> {
            try {
                consumer.accept(new FluxSinkOutputStream(fluxSink, response));
            } catch (Throwable e) {
                fluxSink.error(e);
            }
        })).flatMap(it -> Mono.just(this));
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
        public void flush() throws IOException {
            fluxSink.complete();
        }

        public void writeSink(byte... bytes) {
            fluxSink.next(response.bufferFactory().wrap(bytes));
        }
    }

    @Override
    public OutputStream getOutputStream() {
        return null;
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
}
