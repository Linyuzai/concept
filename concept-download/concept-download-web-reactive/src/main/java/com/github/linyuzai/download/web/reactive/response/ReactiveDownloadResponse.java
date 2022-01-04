package com.github.linyuzai.download.web.reactive.response;

import com.github.linyuzai.download.core.response.DownloadResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * ServerHttpResponse实现 / implementations by ServerHttpResponse
 */
@EnableWebFlux
@Getter
@AllArgsConstructor
public class ReactiveDownloadResponse implements DownloadResponse {

    private ServerHttpResponse response;

    private DataBufferFactory factory;

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new ReactiveOutputStream(response, factory);
    }

    @Override
    public void setStatusCode(int statusCode) {
        response.setStatusCode(HttpStatus.valueOf(statusCode));
    }

    @Override
    public void setContentType(String contentType) {
        addHeader("Content-Type", contentType);
    }

    @Override
    public void setHeader(String name, String value) {
        response.getHeaders().set(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        response.getHeaders().add(name, value);
    }

    public static class ReactiveOutputStream extends OutputStream implements Consumer<FluxSink<DataBuffer>> {

        private final DataBufferFactory factory;

        private FluxSink<DataBuffer> sink;

        public ReactiveOutputStream(ServerHttpResponse response, DataBufferFactory factory) {
            this.factory = factory;
            response.writeWith(Flux.create(this));
        }

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

        public void writeSink(byte... bytes) {
            sink.next(factory.wrap(bytes));
        }

        @Override
        public void accept(FluxSink<DataBuffer> sink) {
            this.sink = sink;
        }

        @Override
        public void close() throws IOException {
            sink.complete();
            super.close();
        }
    }
}
