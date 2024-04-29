package com.github.linyuzai.connection.loadbalance.sse.servlet.okhttp;

import okhttp3.OkHttpClient;

import java.time.Duration;

@Deprecated
public class DefaultOkHttpSseClientFactory implements OkHttpSseClientFactory {

    @Override
    public OkHttpClient create() {
        return new OkHttpClient.Builder()
                .connectTimeout(Duration.ZERO)
                .callTimeout(Duration.ZERO)
                .readTimeout(Duration.ZERO)
                .writeTimeout(Duration.ZERO)
                .build();
    }
}
