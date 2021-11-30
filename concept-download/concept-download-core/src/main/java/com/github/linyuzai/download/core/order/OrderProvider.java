package com.github.linyuzai.download.core.order;

public interface OrderProvider {

    default int getOrder() {
        return 0;
    }
}
