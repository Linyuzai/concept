package com.github.linyuzai.download.core.web.reactive;

import com.github.linyuzai.download.core.concept.ValueContainer;
import lombok.*;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

@NoArgsConstructor
@AllArgsConstructor
public class DownloadMono extends Mono<Void> implements ValueContainer {

    @Setter
    @Getter
    private Object value;

    @Override
    public void subscribe(@NonNull CoreSubscriber<? super Void> actual) {
        //can not be called
    }
}
