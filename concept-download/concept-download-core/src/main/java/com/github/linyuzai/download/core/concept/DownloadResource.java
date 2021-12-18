package com.github.linyuzai.download.core.concept;

import lombok.Getter;
import lombok.Setter;

import java.nio.charset.Charset;

/**
 * 抽象的下载资源 / Abstract download resource
 */
@Getter
@Setter
public abstract class DownloadResource implements Downloadable {

    protected String name;

    protected Charset charset;

    protected boolean cacheEnabled;

    protected String cachePath;

    @SuppressWarnings("unchecked")
    public static class Builder<T extends DownloadResource, B extends Builder<T, B>> {

        protected String name;

        protected Charset charset;

        protected boolean cacheEnabled;

        protected String cachePath;

        public B name(String name) {
            this.name = name;
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
            target.setCharset(charset);
            target.setCacheEnabled(cacheEnabled);
            target.setCachePath(cachePath);
            return target;
        }
    }
}