package com.github.linyuzai.download.classpath;

import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.source.AbstractSource;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 持有一个 {@link ClassPathResource} 的下载源 / Source holds an {@link ClassPathResource}
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassPathResourceSource extends AbstractSource {

    @NonNull
    protected ClassPathResource resource;

    @Override
    public InputStream getInputStream() throws IOException {
        return resource.getInputStream();
    }

    /**
     * 如果没有指定名称 / If no name is specified
     * 将使用资源名称 / The resource name will be used
     *
     * @return 名称 / Name
     */
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
    public Long getLength() {
        long length = resource.contentLength();
        if (length == -1) {
            return null;
        }
        return length;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString() {
        return "ClassPathResourceSource{" +
                "resource=" + resource +
                '}';
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
