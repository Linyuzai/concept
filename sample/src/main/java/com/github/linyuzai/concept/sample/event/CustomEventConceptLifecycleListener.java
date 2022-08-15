package com.github.linyuzai.concept.sample.event;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.lifecycle.EventConceptLifecycleListener;
import org.springframework.stereotype.Component;

//@Component
public class CustomEventConceptLifecycleListener implements EventConceptLifecycleListener {

    @Override
    public void onInitialize(EventConcept concept) {
        //初始化
    }

    @Override
    public void onDestroy(EventConcept concept) {
        //销毁
    }
}
