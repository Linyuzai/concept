package com.github.linyuzai.download.core.order;

import org.springframework.core.Ordered;

/**
 * 提供排序支持。
 */
public interface OrderProvider extends Ordered {

    /**
     * 默认返回 0。
     *
     * @return 0
     */
    default int getOrder() {
        return 0;
    }
}
