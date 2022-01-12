package com.github.linyuzai.download.core.source.classpath;

import com.github.linyuzai.download.core.source.AbstractSource;
import lombok.*;
import org.springframework.core.io.ClassPathResource;
import reactor.core.publisher.Mono;

import java.io.InputStream;

/**
 * 持有一个 {@link ClassPathResource} 的下载源 / Source holds an {@link ClassPathResource}
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassPathResourceSource extends AbstractSource {

    @NonNull
    @Setter
    protected ClassPathResource resource;

    @SneakyThrows
    @Override
    public InputStream getInputStream() {
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

    @SuppressWarnings("unchecked")
    public static class Builder<T extends ClassPathResourceSource, B extends Builder<T, B>> extends AbstractSource.Builder<T, B> {

        private ClassPathResource resource;

        public B resource(ClassPathResource resource) {
            this.resource = resource;
            return (B) this;
        }

        @Override
        protected T build(T target) {
            target.setResource(resource);
            return super.build(target);
        }

        @Override
        public T build() {
            return build((T) new ClassPathResourceSource());
        }
    }
}
