package com.github.linyuzai.concept.sample.event;

import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.context.EventContextFactory;
import org.springframework.stereotype.Component;

//@Component
public class CustomEventContextFactory implements EventContextFactory {

    @Override
    public EventContext create() {
        //自定义
        return null;
    }
}
