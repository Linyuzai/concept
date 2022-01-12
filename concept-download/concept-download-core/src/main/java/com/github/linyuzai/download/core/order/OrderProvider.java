package com.github.linyuzai.download.core.order;

import org.springframework.core.Ordered;

/**
 * 提供排序支持 / Provide sorting support
 */
public interface OrderProvider extends Ordered {

    default int getOrder() {
        return 0;
    }
}
