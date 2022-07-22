package com.github.linyuzai.concept.sample.event.kafka;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.kafka.exchange.KafkaEngineExchange;
import com.github.linyuzai.event.kafka.endpoint.KafkaEventEndpoint;
import com.github.linyuzai.event.kafka.subscriber.TopicKafkaEventSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.Acknowledgment;

@Configuration
public class KafkaEventSubscriberRegister implements ApplicationRunner {

    @Autowired
    public EventConcept concept;

    //@KafkaListener(topics = "sample", containerFactory = "localKafkaListenerContainerFactory")
    public void receiveLocal(String msg, Acknowledgment acknowledgment) {
        System.out.println("local-" + msg);
        acknowledgment.acknowledge();
    }

    //@KafkaListener(topics = "sample", containerFactory = "devKafkaListenerContainerFactory")
    public void receiveDev(String msg, Acknowledgment acknowledgment) {
        System.out.println("dev-" + msg);
        acknowledgment.acknowledge();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        concept.event(String.class)
                .exchange(new KafkaEngineExchange())
                .subscribe(new TopicKafkaEventSubscriber<String>("sample") {
                    @Override
                    public void onEvent(String event, KafkaEventEndpoint endpoint, EventContext context) {
                        System.out.println("subscribe-" + endpoint.getName() + ":" + event);
                    }
                });
    }
}
