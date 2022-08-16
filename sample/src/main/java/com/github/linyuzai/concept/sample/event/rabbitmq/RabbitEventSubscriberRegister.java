package com.github.linyuzai.concept.sample.event.rabbitmq;

import com.github.linyuzai.event.core.concept.EventConcept;
import com.github.linyuzai.event.core.context.EventContext;
import com.github.linyuzai.event.core.endpoint.EventEndpoint;
import com.github.linyuzai.event.core.listener.GenericEventListener;
import com.github.linyuzai.event.rabbitmq.binding.RabbitBinding;
import com.github.linyuzai.event.rabbitmq.exchange.RabbitEngineExchange;
import com.github.linyuzai.event.rabbitmq.subscriber.QueueRabbitEventSubscriber;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitEventSubscriberRegister implements ApplicationRunner {

    @Autowired
    public EventConcept concept;

    @SneakyThrows
    //@RabbitListener(queues = "queue", containerFactory = "devRabbitListenerContainerFactory")
    public void receiveDev(Message message, Channel channel) {
        System.out.println("dev-" + new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        concept.template()
                .exchange(new RabbitEngineExchange())
                .subscriber(new QueueRabbitEventSubscriber("concept-event") {

                    @Override
                    public void binding(RabbitBinding binding) {
                        binding.bind(new Queue("concept-event"))
                                .to(new TopicExchange("concept-event"))
                                .with("concept-event.#");
                    }
                })
                .subscribe(new GenericEventListener<RabbitData>() {
                    @Override
                    public void onGenericEvent(RabbitData event, EventEndpoint endpoint, EventContext context) {
                        System.out.println(event);
                    }
                });
    }
}
