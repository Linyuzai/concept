package com.github.linyuzai.connection.loadbalance.sse.servlet.okhttp;

import okhttp3.OkHttpClient;

@Deprecated
public interface OkHttpSseClientFactory {

    OkHttpClient create();
}
