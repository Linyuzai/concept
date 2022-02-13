package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.concept.AbstractResource;
import lombok.Getter;
import lombok.Setter;

/**
 * {@link Source} 的抽象类。
 */
@Getter
@Setter
public abstract class AbstractSource extends AbstractResource implements Source {

    /**
     * 异步加载
     */
    protected boolean asyncLoad;

    @SuppressWarnings("unchecked")
    public static abstract class Builder<T extends AbstractSource, B extends Builder<T, B>> extends AbstractResource.Builder<T, B> {

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
