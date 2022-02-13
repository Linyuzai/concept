package com.github.linyuzai.download.core.source.classpath;

import com.github.linyuzai.download.core.source.AbstractSource;
import com.github.linyuzai.download.core.source.Source;
import lombok.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.InputStream;

/**
 * 持有 {@link ClassPathResource} 的 {@link Source}
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassPathSource extends AbstractSource {

    /**
     * 持有的 {@link ClassPathResource}
     */
    @NonNull
    @Setter
    protected ClassPathResource resource;

    /**
     * 直接调用 {@link ClassPathResource#getInputStream()}。
     *
     * @return {@link ClassPathResource#getInputStream()}
     */
    @SneakyThrows
    @Override
    public InputStream openInputStream() {
        return resource.getInputStream();
    }

    /**
     * 如果没有指定名称则使用 {@link ClassPathResource#getFilename()}。
     *
     * @return 指定的名称或 {@link ClassPathResource#getFilename()}
     */
    @Override
    public String getName() {
        String name = super.getName();
        if (!StringUtils.hasText(name)) {
            setName(resource.getFilename());
        }
        return super.getName();
    }

    /**
     * 调用 {@link ClassPathResource#contentLength()} 获得长度，
     * 如果为 -1 则返回 null。
     *
     * @return 长度或 null
     */
    @SneakyThrows
    @Override
    public Long getLength() {
        long length = resource.contentLength();
        if (length == -1) {
            return null;
        }
        return length;
    }

    /**
     * 直接返回 true。
     *
     * @return true
     */
    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String getDescription() {
        return "ClassPathSource(" + resource.getPath() + ")";
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends ClassPathSource, B extends Builder<T, B>> extends AbstractSource.Builder<T, B> {

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
            return build((T) new ClassPathSource());
        }
    }
}
