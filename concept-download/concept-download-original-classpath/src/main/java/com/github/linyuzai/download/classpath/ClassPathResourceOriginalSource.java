package com.github.linyuzai.download.classpath;

import com.github.linyuzai.download.core.original.AbstractOriginalSource;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.writer.SourceWriter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class ClassPathResourceOriginalSource extends AbstractOriginalSource {

    private final ClassPathResource resource;

    private ClassPathResourceOriginalSource(@NonNull ClassPathResource resource, Charset charset, boolean asyncLoad) {
        this.resource = resource;
        setCharset(charset);
        setAsyncLoad(asyncLoad);
    }

    @Override
    public String getName() {
        return resource.getFilename();
    }

    @SneakyThrows
    @Override
    public long getLength() {
        return resource.contentLength();
    }

    @Override
    public void load() throws IOException {

    }

    @Override
    public void write(OutputStream os, Range range, SourceWriter writer, WriteHandler handler) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            Part part = new Part() {

                @Override
                public InputStream getInputStream() throws IOException {
                    return is;
                }

                @Override
                public String getName() {
                    return ClassPathResourceOriginalSource.this.getName();
                }

                @Override
                public String getPath() {
                    return ClassPathResourceOriginalSource.this.getName();
                }

                @Override
                public Charset getCharset() {
                    return ClassPathResourceOriginalSource.this.getCharset();
                }

                @Override
                public void write() throws IOException {
                    writer.write(getInputStream(), os, range, getCharset(), ClassPathResourceOriginalSource.this.getLength());
                }
            };
            handler.handle(part);
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    public static class Builder {

        private ClassPathResource resource;

        private Charset charset;

        private boolean asyncLoad;

        public Builder resource(ClassPathResource resource) {
            this.resource = resource;
            return this;
        }

        public Builder charset(Charset charset) {
            this.charset = charset;
            return this;
        }

        public Builder asyncLoad(boolean asyncLoad) {
            this.asyncLoad = asyncLoad;
            return this;
        }

        public ClassPathResourceOriginalSource build() {
            return new ClassPathResourceOriginalSource(resource, charset, asyncLoad);
        }
    }
}
