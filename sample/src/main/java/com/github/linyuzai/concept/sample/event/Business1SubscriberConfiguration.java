package com.github.linyuzai.concept.sample.event;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.listener.GenericEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

//@Component
public class Business1SubscriberConfiguration implements ApplicationRunner {

    @Autowired
    private EventConcept concept;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        concept.template()
                .exchange(new Business1EventExchange())
                .subscriber(new Business1EventSubscriber())
                .subscribe(new GenericEventListener<Business1Event>() {
                    @Override
                    public void onGenericEvent(Business1Event event, EventEndpoint endpoint, EventContext context) {
                        //可以通过EventEndpoint获取消息的来源
                    }
                });
    }
}
