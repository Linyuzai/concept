package com.github.linyuzai.domain.core.cache;

/**
 * 缓存
 */
public interface Cache<T> {

    /**
     * 设置缓存
     */
    void set(String id, T cache);

    /**
     * 获得缓存
     */
    T get(String id);

    /**
     * 缓存是否存在
     */
    boolean exist(String id);

    /**
     * 清除缓存
     */
    void remove(String id);

    /**
     * 清空缓存
     */
    void clear();

    @SuppressWarnings("unchecked")
    static <T> Cache<T> disabled() {
        return (Cache<T>) DISABLED;
    }

    Cache<?> DISABLED = new Cache<>() {

        @Override
        public void set(String id, Object cache) {

        }

        @Override
        public Object get(String id) {
            return null;
        }

        @Override
        public boolean exist(String id) {
            return false;
        }

        @Override
        public void remove(String id) {

        }

        @Override
        public void clear() {

        }
    };
}
