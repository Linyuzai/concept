package com.github.linyuzai.connection.loadbalance.sse.servlet.okhttp;

import okhttp3.OkHttpClient;

public interface OkHttpSseClientFactory {

    OkHttpClient create();
}
