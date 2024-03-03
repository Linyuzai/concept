package com.github.linyuzai.connection.loadbalance.sse.servlet.okhttp;

import okhttp3.OkHttpClient;

public class DefaultOkHttpSseClientFactory implements OkHttpSseClientFactory {

    @Override
    public OkHttpClient create() {
        return new OkHttpClient();
    }
}
