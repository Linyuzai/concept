package com.github.linyuzai.download.classpath;

import com.github.linyuzai.download.core.source.AbstractSource;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassPathResourceSource extends AbstractSource {

    @NonNull
    protected final ClassPathResource resource;

    @Override
    public String getName() {
        String name = super.getName();
        if (name == null || name.isEmpty()) {
            return resource.getFilename();
        } else {
            return name;
        }
    }

    @SneakyThrows
    @Override
    public long getLength() {
        return resource.contentLength();
    }

    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            Part part = new Part() {

                @Override
                public InputStream getInputStream() throws IOException {
                    return is;
                }

                @Override
                public String getName() {
                    return ClassPathResourceSource.this.getName();
                }

                @Override
                public String getPath() {
                    return ClassPathResourceSource.this.getName();
                }

                @Override
                public Charset getCharset() {
                    return ClassPathResourceSource.this.getCharset();
                }

                @Override
                public void write() throws IOException {
                    writer.write(getInputStream(), os, range, getCharset(), ClassPathResourceSource.this.getLength());
                }
            };
            handler.handle(part);
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    public static class Builder extends AbstractSource.Builder<ClassPathResourceSource, Builder> {

        private ClassPathResource resource;

        public Builder resource(ClassPathResource resource) {
            this.resource = resource;
            return this;
        }

        public ClassPathResourceSource build() {
            return super.build(new ClassPathResourceSource(resource));
        }
    }
}
