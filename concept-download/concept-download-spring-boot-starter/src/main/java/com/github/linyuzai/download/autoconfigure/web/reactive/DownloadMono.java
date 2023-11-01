package com.github.linyuzai.download.autoconfigure.web.reactive;

import com.github.linyuzai.download.core.concept.ValueContainer;
import lombok.*;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

/**
 * 为了通过 webflux 的参数类型校验，需要返回该对象，并传入真实的返回值。
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DownloadMono extends Mono<Void> implements ValueContainer {

    /**
     * 真实的返回值
     */
    @Getter
    private Object value;

    @Override
    public void subscribe(@NonNull CoreSubscriber<? super Void> actual) {
        //can not be called
    }

    public static @NonNull DownloadMono empty() {
        return new DownloadMono();
    }

    public static DownloadMono value(Object value) {
        return new DownloadMono(value);
    }
}
