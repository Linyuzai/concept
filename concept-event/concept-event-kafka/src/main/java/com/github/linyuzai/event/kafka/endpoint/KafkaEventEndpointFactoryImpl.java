package com.github.linyuzai.event.kafka.endpoint;

import com.github.linyuzai.event.kafka.engine.KafkaEventEngine;
import com.github.linyuzai.event.kafka.properties.KafkaEventProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.security.jaas.KafkaJaasLoginModuleInitializer;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;

@Getter
@AllArgsConstructor
public class KafkaEventEndpointFactoryImpl implements KafkaEventEndpointFactory {

    private final ObjectProvider<RecordMessageConverter> messageConverter;

    @Override
    public KafkaEventEndpoint create(String name,
                                     KafkaEventProperties.ExtendedKafkaProperties properties,
                                     KafkaEventEngine engine) {
        ProducerFactory<Object, Object> producerFactory = createProducerFactory(properties);

        ProducerListener<Object, Object> producerListener = createProducerListener();

        KafkaTemplate<Object, Object> kafkaTemplate =
                createKafkaTemplate(properties, producerFactory, producerListener);

        ConsumerFactory<Object, Object> consumerFactory = createConsumerFactory(properties);

        KafkaTransactionManager<Object, Object> kafkaTransactionManager =
                createKafkaTransactionManager(properties, producerFactory);

        KafkaListenerContainerFactory<? extends MessageListenerContainer> kafkaListenerContainerFactory =
                createKafkaListenerContainerFactory(properties, consumerFactory, kafkaTransactionManager);

        KafkaAdmin kafkaAdmin = createKafkaAdmin(properties);

        //registerKafkaJaasLoginModuleInitializer(key, value, beanFactory);

        KafkaEventEndpoint endpoint = new KafkaEventEndpoint(name, engine);
        endpoint.setProperties(properties);
        endpoint.setProducerFactory(producerFactory);
        endpoint.setProducerListener(producerListener);
        endpoint.setTemplate(kafkaTemplate);
        endpoint.setConsumerFactory(consumerFactory);
        endpoint.setTransactionManager(kafkaTransactionManager);
        endpoint.setListenerContainerFactory(kafkaListenerContainerFactory);
        endpoint.setAdmin(kafkaAdmin);

        properties.apply(endpoint);

        return endpoint;
    }

    private ProducerFactory<Object, Object> createProducerFactory(
            KafkaEventProperties.ExtendedKafkaProperties properties) {
        DefaultKafkaProducerFactory<Object, Object> producerFactory = new DefaultKafkaProducerFactory<>(
                properties.buildProducerProperties());
        String transactionIdPrefix = properties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            producerFactory.setTransactionIdPrefix(transactionIdPrefix);
        }
        return producerFactory;
    }

    private ProducerListener<Object, Object> createProducerListener() {
        return new LoggingProducerListener<>();
    }

    private KafkaTemplate<Object, Object> createKafkaTemplate(KafkaEventProperties.ExtendedKafkaProperties properties,
                                                              ProducerFactory<Object, Object> producerFactory,
                                                              ProducerListener<Object, Object> producerListener) {
        KafkaTemplate<Object, Object> template = new KafkaTemplate<>(producerFactory);
        messageConverter.ifUnique(template::setMessageConverter);
        template.setProducerListener(producerListener);
        template.setDefaultTopic(properties.getTemplate().getDefaultTopic());
        return template;
    }

    private ConsumerFactory<Object, Object> createConsumerFactory(
            KafkaEventProperties.ExtendedKafkaProperties properties) {
        return new DefaultKafkaConsumerFactory<>(properties.buildConsumerProperties());
    }

    private KafkaListenerContainerFactory<? extends MessageListenerContainer> createKafkaListenerContainerFactory(
            KafkaEventProperties.ExtendedKafkaProperties properties,
            ConsumerFactory<Object, Object> consumerFactory,
            KafkaTransactionManager<Object, Object> transactionManager) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> listenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        listenerContainerFactory.setConsumerFactory(consumerFactory);
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        KafkaEventProperties.ExtendedKafkaProperties.Listener listenerProperties = properties.getListener();
        map.from(listenerProperties::getConcurrency).to(listenerContainerFactory::setConcurrency);
        ContainerProperties container = listenerContainerFactory.getContainerProperties();
        container.setTransactionManager(transactionManager);
        map.from(listenerProperties::getAckMode).to(container::setAckMode);
        map.from(listenerProperties::getClientId).to(container::setClientId);
        map.from(listenerProperties::getAckCount).to(container::setAckCount);
        map.from(listenerProperties::getAckTime).as(Duration::toMillis).to(container::setAckTime);
        map.from(listenerProperties::getPollTimeout).as(Duration::toMillis).to(container::setPollTimeout);
        map.from(listenerProperties::getNoPollThreshold).to(container::setNoPollThreshold);
        map.from(listenerProperties.getIdleBetweenPolls()).as(Duration::toMillis).to(container::setIdleBetweenPolls);
        map.from(listenerProperties::getIdleEventInterval).as(Duration::toMillis).to(container::setIdleEventInterval);
        map.from(listenerProperties::getMonitorInterval).as(Duration::getSeconds).as(Number::intValue)
                .to(container::setMonitorInterval);
        map.from(listenerProperties::getLogContainerConfig).to(container::setLogContainerConfig);
        if (ClassUtils.hasMethod(KafkaProperties.Listener.class, "isOnlyLogRecordMetadata") &&
                ClassUtils.hasMethod(KafkaProperties.Listener.class, "setOnlyLogRecordMetadata")) {
            map.from(listenerProperties::isOnlyLogRecordMetadata).to(container::setOnlyLogRecordMetadata);
        }
        map.from(listenerProperties::isMissingTopicsFatal).to(container::setMissingTopicsFatal);
        return listenerContainerFactory;
    }

    private KafkaTransactionManager<Object, Object> createKafkaTransactionManager(
            KafkaEventProperties.ExtendedKafkaProperties properties,
            ProducerFactory<Object, Object> producerFactory) {
        if (StringUtils.hasText(properties.getProducer().getTransactionIdPrefix())) {
            //ChainedKafkaTransactionManager
            return new KafkaTransactionManager<>(producerFactory);
        }
        return null;
    }

    private KafkaAdmin createKafkaAdmin(KafkaEventProperties.ExtendedKafkaProperties properties) {
        KafkaAdmin admin = new KafkaAdmin(properties.buildAdminProperties());
        admin.setFatalIfBrokerNotAvailable(properties.getAdmin().isFailFast());
        return admin;
    }

    @Deprecated
    @SneakyThrows
    private void registerKafkaJaasLoginModuleInitializer(String key,
                                                         KafkaEventProperties.ExtendedKafkaProperties properties,
                                                         ConfigurableBeanFactory beanFactory) {
        if (properties.getJaas().isEnabled()) {
            KafkaJaasLoginModuleInitializer jaas = new KafkaJaasLoginModuleInitializer();
            KafkaEventProperties.ExtendedKafkaProperties.Jaas jaasProperties = properties.getJaas();
            if (jaasProperties.getControlFlag() != null) {
                jaas.setControlFlag(jaasProperties.getControlFlag());
            }
            if (jaasProperties.getLoginModule() != null) {
                jaas.setLoginModule(jaasProperties.getLoginModule());
            }
            jaas.setOptions(jaasProperties.getOptions());
            beanFactory.registerSingleton(key + "KafkaJaasLoginModuleInitializer", jaas);
        }
    }
}
