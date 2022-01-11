package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.concept.DownloadResource;
import lombok.Getter;
import lombok.Setter;

/**
 * 抽象的数据源 / Abstract source
 */
@Getter
@Setter
public abstract class AbstractSource extends DownloadResource implements Source {

    protected boolean asyncLoad;

    @SuppressWarnings("unchecked")
    public static abstract class Builder<T extends AbstractSource, B extends Builder<T, B>> extends DownloadResource.Builder<T, B> {

        protected boolean asyncLoad;

        public B asyncLoad(boolean asyncLoad) {
            this.asyncLoad = asyncLoad;
            return (B) this;
        }

        @Override
        protected T build(T target) {
            target.setAsyncLoad(asyncLoad);
            return super.build(target);
        }

        public abstract T build();
    }
}
