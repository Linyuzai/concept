package com.github.linyuzai.download.core.order;

/**
 * 提供排序支持 / Provide sorting support
 */
public interface OrderProvider {

    default int getOrder() {
        return 0;
    }
}
