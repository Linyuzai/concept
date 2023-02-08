package com.github.linyuzai.domain.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 领域集合抽象类
 *
 * @param <T>
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractDomainCollection<T extends DomainObject> implements DomainCollection<T> {

    /**
     * 所属者
     */
    protected Object owner;

    /**
     * 领域模型缓存
     */
    protected Map<String, T> objects;

    /**
     * 根据 id 获得 领域模型
     */
    @Override
    public T get(String id) {
        return objects.get(id);
    }

    /**
     * 流式读取
     */
    @Override
    public Stream<? extends T> stream() {
        return objects.values().stream();
    }

    /**
     * 获得数量
     */
    @Override
    public Long count() {
        return Integer.valueOf(objects.size()).longValue();
    }

    /**
     * 领域集合 Builder
     *
     * @param <T>
     * @param <C>
     * @param <B>
     */
    @SuppressWarnings("unchecked")
    protected static abstract class Builder<T extends DomainObject, C extends DomainCollection<T>, B extends Builder<T, C, B>> extends AbstractDomainBuilder<C, B> {

        @NotNull
        protected Object owner;

        @NotNull
        protected Collection<? extends T> objects;

        public B owner(Object owner) {
            this.owner = owner;
            return (B) this;
        }

        public B objects(Collection<? extends T> objects) {
            this.objects = objects;
            return (B) this;
        }

        @Override
        protected void initDefaultValue() {
            if (objects == null) {
                objects = new ArrayList<>();
            }
        }

        protected Map<String, T> getObjectMap() {
            return objects.stream()
                    .collect(Collectors.toMap(DomainObject::getId,
                            Function.identity()));
        }
    }
}
