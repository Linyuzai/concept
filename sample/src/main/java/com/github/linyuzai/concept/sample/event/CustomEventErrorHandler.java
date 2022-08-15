package com.github.linyuzai.concept.sample.event;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import org.springframework.stereotype.Component;

//@Component
public class CustomEventErrorHandler implements EventErrorHandler {

    @Override
    public void onError(Throwable e, EventEndpoint endpoint, EventContext context) {
        //自定义异常处理
    }
}
