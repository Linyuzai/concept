package com.github.linyuzai.download.core.concept;

import lombok.Getter;
import lombok.Setter;

import java.nio.charset.Charset;

/**
 * {@link Downloadable} 的抽象类。
 */
@Getter
@Setter
public abstract class AbstractDownloadable extends AbstractPart implements Downloadable {

    /**
     * 名称。
     */
    protected String name;

    /**
     * Content-Type
     */
    protected String contentType;

    /**
     * 编码。
     */
    protected Charset charset;

    /**
     * 缓存是否启用。
     */
    protected boolean cacheEnabled;

    /**
     * 缓存路径。
     */
    protected String cachePath;

    @SuppressWarnings("unchecked")
    public static class Builder<T extends AbstractDownloadable, B extends Builder<T, B>> {

        protected String name;

        protected String contentType;

        protected Charset charset;

        protected boolean cacheEnabled;

        protected String cachePath;

        public B name(String name) {
            this.name = name;
            return (B) this;
        }

        public B contentType(String contentType) {
            this.contentType = contentType;
            return (B) this;
        }

        public B charset(Charset charset) {
            this.charset = charset;
            return (B) this;
        }

        public B cacheEnabled(boolean cacheEnabled) {
            this.cacheEnabled = cacheEnabled;
            return (B) this;
        }

        public B cachePath(String cachePath) {
            this.cachePath = cachePath;
            return (B) this;
        }

        protected T build(T target) {
            target.setName(name);
            target.setContentType(contentType);
            target.setCharset(charset);
            target.setCacheEnabled(cacheEnabled);
            target.setCachePath(cachePath);
            return target;
        }
    }
}
