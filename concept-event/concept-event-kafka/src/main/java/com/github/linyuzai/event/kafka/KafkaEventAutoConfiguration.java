package com.github.linyuzai.event.kafka;

import com.github.linyuzai.event.core.publisher.EventPublisherGroup;
import lombok.SneakyThrows;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(KafkaEventProperties.class)
@AutoConfigureBefore(KafkaAutoConfiguration.class)
public class KafkaEventAutoConfiguration {

    @Bean
    public KafkaEventPublisherGroup kafkaEventPublisherGroup(ConfigurableBeanFactory beanFactory,
                                                        KafkaEventProperties properties,
                                                        ObjectProvider<RecordMessageConverter> messageConverter) throws IOException {
        KafkaEventPublisherGroup publisherGroup = new KafkaEventPublisherGroup();
        for (Map.Entry<String, KafkaProperties> entry : properties.getKafka().entrySet()) {
            String key = entry.getKey();
            KafkaProperties value = entry.getValue();

            ProducerFactory<Object, Object> producerFactory =
                    registerProducerFactory(key, value, beanFactory);

            ProducerListener<Object, Object> producerListener =
                    registerProducerListener(key, beanFactory);

            KafkaTemplate<Object, Object> kafkaTemplate =
                    registerKafkaTemplate(key, value, messageConverter,
                            producerFactory, producerListener, beanFactory);

            ConsumerFactory<Object, Object> consumerFactory =
                    registerConsumerFactory(key, value, beanFactory);

            KafkaTransactionManager<Object, Object> kafkaTransactionManager =
                    registerKafkaTransactionManager(key, value, producerFactory, beanFactory);

            KafkaListenerContainerFactory<? extends MessageListenerContainer> listenerContainerFactory =
                    registerKafkaListenerContainerFactory(key, value, consumerFactory,
                            kafkaTransactionManager, beanFactory);

            KafkaAdmin kafkaAdmin = registerKafkaAdmin(key, value, beanFactory);

            registerKafkaJaasLoginModuleInitializer(key, value, beanFactory);

            KafkaEventPublisher publisher = new KafkaEventPublisher(kafkaTemplate);
            publisherGroup.add(publisher);
        }

        return publisherGroup;
    }

    private ProducerFactory<Object, Object> registerProducerFactory(String key,
                                                                    KafkaProperties properties,
                                                                    ConfigurableBeanFactory beanFactory) {
        DefaultKafkaProducerFactory<Object, Object> producerFactory = new DefaultKafkaProducerFactory<>(
                properties.buildProducerProperties());
        String transactionIdPrefix = properties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            producerFactory.setTransactionIdPrefix(transactionIdPrefix);
        }
        beanFactory.registerSingleton(key + "ProducerFactory", producerFactory);
        return producerFactory;
    }

    private ProducerListener<Object, Object> registerProducerListener(String key,
                                                                      ConfigurableBeanFactory beanFactory) {
        ProducerListener<Object, Object> producerListener = new LoggingProducerListener<>();
        beanFactory.registerSingleton(key + "ProducerListener", producerListener);
        return producerListener;
    }

    private KafkaTemplate<Object, Object> registerKafkaTemplate(String key,
                                                                KafkaProperties properties,
                                                                ObjectProvider<RecordMessageConverter> messageConverter,
                                                                ProducerFactory<Object, Object> producerFactory,
                                                                ProducerListener<Object, Object> producerListener,
                                                                ConfigurableBeanFactory beanFactory) {
        KafkaTemplate<Object, Object> template = new KafkaTemplate<>(producerFactory);
        messageConverter.ifUnique(template::setMessageConverter);
        template.setProducerListener(producerListener);
        template.setDefaultTopic(properties.getTemplate().getDefaultTopic());
        beanFactory.registerSingleton(key + "KafkaTemplate", template);
        return template;
    }

    private ConsumerFactory<Object, Object> registerConsumerFactory(String key,
                                                                    KafkaProperties properties,
                                                                    ConfigurableBeanFactory beanFactory) {
        DefaultKafkaConsumerFactory<Object, Object> consumerFactory = new DefaultKafkaConsumerFactory<>(
                properties.buildConsumerProperties());
        beanFactory.registerSingleton(key + "ConsumerFactory", consumerFactory);
        return consumerFactory;
    }

    private KafkaListenerContainerFactory<? extends MessageListenerContainer> registerKafkaListenerContainerFactory(
            String key,
            KafkaProperties properties,
            ConsumerFactory<Object, Object> consumerFactory,
            KafkaTransactionManager<Object, Object> transactionManager,
            ConfigurableBeanFactory beanFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> listenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        listenerContainerFactory.setConsumerFactory(consumerFactory);
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        KafkaProperties.Listener listenerProperties = properties.getListener();
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
        map.from(listenerProperties::isOnlyLogRecordMetadata).to(container::setOnlyLogRecordMetadata);
        map.from(listenerProperties::isMissingTopicsFatal).to(container::setMissingTopicsFatal);
        beanFactory.registerSingleton(key + "KafkaListenerContainerFactory", listenerContainerFactory);
        return listenerContainerFactory;
    }

    private KafkaTransactionManager<Object, Object> registerKafkaTransactionManager(String key,
                                                                                    KafkaProperties properties,
                                                                                    ProducerFactory<Object, Object> producerFactory,
                                                                                    ConfigurableBeanFactory beanFactory) {
        if (StringUtils.hasText(properties.getProducer().getTransactionIdPrefix())) {
            KafkaTransactionManager<Object, Object> transactionManager =
                    new KafkaTransactionManager<>(producerFactory);
            beanFactory.registerSingleton(key + "KafkaTransactionManager", transactionManager);
            return transactionManager;
        }
        return null;
    }

    private KafkaAdmin registerKafkaAdmin(String key,
                                          KafkaProperties properties,
                                          ConfigurableBeanFactory beanFactory) {
        KafkaAdmin admin = new KafkaAdmin(properties.buildAdminProperties());
        admin.setFatalIfBrokerNotAvailable(properties.getAdmin().isFailFast());
        beanFactory.registerSingleton(key + "KafkaAdmin", admin);
        return admin;
    }

    @SneakyThrows
    private void registerKafkaJaasLoginModuleInitializer(String key,
                                                         KafkaProperties properties,
                                                         ConfigurableBeanFactory beanFactory) {
        if (properties.getJaas().isEnabled()) {
            KafkaJaasLoginModuleInitializer jaas = new KafkaJaasLoginModuleInitializer();
            KafkaProperties.Jaas jaasProperties = properties.getJaas();
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

    //ChainedKafkaTransactionManager
}
