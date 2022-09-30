package com.github.linyuzai.extension.core.lifecycle;

/**
 * 生命周期接口
 * <p>
 * 部分场景下提供初始化和销毁的自动触发
 */
public interface Lifecycle {

    /**
     * 初始化
     */
    void initialize();

    boolean initialized();

    /**
     * 销毁
     */
    void destroy();

    default void refresh() {
        destroy();
        initialize();
    }
}
