package com.github.linyuzai.download.core.concept;

/**
 * 值容器，用于方法返回值。
 * <p>
 * Value container for method return values.
 */
public interface ValueContainer {

    /**
     * 获得真实的返回值。
     * <p>
     * Get the real return value.
     *
     * @return 真实的返回值
     * <p>
     * The real return value
     */
    Object getValue();
}
