package com.github.linyuzai.event.kafka.properties;

import com.github.linyuzai.event.core.codec.EventDecoder;
import com.github.linyuzai.event.core.codec.EventEncoder;
import com.github.linyuzai.event.core.concept.EventOperator;
import com.github.linyuzai.event.core.error.EventErrorHandler;
import com.github.linyuzai.event.core.publisher.EventPublisher;
import com.github.linyuzai.event.core.subscriber.EventSubscriber;
import com.github.linyuzai.event.kafka.exception.KafkaEventException;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ConfigurationProperties("concept.event.kafka")
public class KafkaEventProperties implements EventOperator.PropertyConfig {

    private boolean enabled = true;

    private InheritProperties inherit = new InheritProperties();

    private Map<Object, Object> metadata = new LinkedHashMap<>();

    private Class<? extends EventEncoder> encoder;

    private Class<? extends EventDecoder> decoder;

    private Class<? extends EventErrorHandler> errorHandler;

    private Class<? extends EventPublisher> publisher;

    private Class<? extends EventSubscriber> subscriber;

    private Map<String, ExtendedKafkaProperties> endpoints = new LinkedHashMap<>();

    public void inherit(Environment environment) {
        for (Map.Entry<String, ExtendedKafkaProperties> entry : endpoints.entrySet()) {
            String name = entry.getKey();
            ExtendedKafkaProperties properties = entry.getValue();
            if (StringUtils.hasText(properties.getInherit())) {
                ExtendedKafkaProperties inherit = endpoints.get(properties.getInherit());
                if (inherit == null) {
                    throw new KafkaEventException("Inherit not found: " + properties.getInherit());
                }
                properties.inherit(name, inherit, environment);
            }
        }
    }

    @Data
    public static class InheritProperties {

        private boolean enabled = true;
    }

    @Getter
    @Setter
    public static class ExtendedKafkaProperties extends KafkaProperties implements EventOperator.PropertyConfig {

        private boolean enabled = true;

        private String inherit;

        private Map<Object, Object> metadata = new LinkedHashMap<>();

        private Class<? extends EventEncoder> encoder;

        private Class<? extends EventDecoder> decoder;

        private Class<? extends EventErrorHandler> errorHandler;

        private Class<? extends EventPublisher> publisher;

        private Class<? extends EventSubscriber> subscriber;

        public void inherit(String name, ExtendedKafkaProperties parent, Environment environment) {
            String prefix = "concept.event.kafka.endpoints." + name;
            inheritProperties(getProperties(), parent.getProperties());
            inheritConsumer(getConsumer(), parent.getConsumer(), environment, prefix + ".consumer");
            inheritProducer(getProducer(), parent.getProducer(), environment, prefix + ".producer");
            inheritListener(getListener(), parent.getListener(), environment, prefix + ".listener");
            inheritAdmin(getAdmin(), parent.getAdmin(), environment, prefix + ".admin");
            inheritStreams(getStreams(), parent.getStreams(), environment, prefix + ".streams");
            inheritSsl(getSsl(), parent.getSsl());
            //jaas好像是全局的，可以配置在spring.kafka中
            inheritTemplate(getTemplate(), parent.getTemplate());
            inheritSecurity(getSecurity(), parent.getSecurity());
            inheritExtended(this, parent);
        }

        public <K, V> void inheritProperties(Map<K, V> child, Map<K, V> parent) {
            for (Map.Entry<K, V> entry : parent.entrySet()) {
                K key = entry.getKey();
                if (child.containsKey(key)) {
                    continue;
                }
                child.put(key, entry.getValue());
            }
        }

        public void inheritConsumer(Consumer child, Consumer parent, Environment environment, String prefix) {
            inheritSsl(child.getSsl(), parent.getSsl());
            inheritSecurity(child.getSecurity(), parent.getSecurity());
            if (child.getAutoCommitInterval() == null) {
                child.setAutoCommitInterval(parent.getAutoCommitInterval());
            }
            if (child.getAutoOffsetReset() == null) {
                child.setAutoOffsetReset(parent.getAutoOffsetReset());
            }
            if (child.getEnableAutoCommit() == null) {
                child.setEnableAutoCommit(parent.getEnableAutoCommit());
            }
            if (child.getFetchMaxWait() == null) {
                child.setFetchMaxWait(parent.getFetchMaxWait());
            }
            if (child.getFetchMinSize() == null) {
                child.setFetchMinSize(parent.getFetchMinSize());
            }
            if (child.getGroupId() == null) {
                child.setGroupId(parent.getGroupId());
            }
            if (child.getHeartbeatInterval() == null) {
                child.setHeartbeatInterval(parent.getHeartbeatInterval());
            }
            String isolationLevel = environment.getProperty(prefix + ".isolation-level");
            if (isolationLevel == null) {
                child.setIsolationLevel(parent.getIsolationLevel());
            }
            String keyDeserializer = environment.getProperty(prefix + ".key-deserializer");
            if (keyDeserializer == null) {
                child.setKeyDeserializer(parent.getKeyDeserializer());
            }
            String valueDeserializer = environment.getProperty(prefix + ".value-deserializer");
            if (valueDeserializer == null) {
                child.setValueDeserializer(parent.getValueDeserializer());
            }
            if (child.getMaxPollRecords() == null) {
                child.setMaxPollRecords(parent.getMaxPollRecords());
            }
            inheritProperties(child.getProperties(), parent.getProperties());
        }

        public void inheritProducer(Producer child, Producer parent, Environment environment, String prefix) {
            inheritSsl(child.getSsl(), parent.getSsl());
            inheritSecurity(child.getSecurity(), parent.getSecurity());
            if (child.getAcks() == null) {
                child.setAcks(parent.getAcks());
            }
            if (child.getBatchSize() == null) {
                child.setBatchSize(parent.getBatchSize());
            }
            if (child.getBufferMemory() == null) {
                child.setBufferMemory(parent.getBufferMemory());
            }
            if (child.getCompressionType() == null) {
                child.setCompressionType(parent.getCompressionType());
            }
            String keySerializer = environment.getProperty(prefix + ".key-serializer");
            if (keySerializer == null) {
                child.setKeySerializer(parent.getKeySerializer());
            }
            String valueSerializer = environment.getProperty(prefix + ".value-serializer");
            if (valueSerializer == null) {
                child.setValueSerializer(parent.getValueSerializer());
            }
            if (child.getRetries() == null) {
                child.setRetries(parent.getRetries());
            }
            inheritProperties(child.getProperties(), parent.getProperties());
        }

        public void inheritListener(Listener child, Listener parent, Environment environment, String prefix) {
            String type = environment.getProperty(prefix + ".type");
            if (type == null) {
                child.setType(parent.getType());
            }
            if (child.getAckMode() == null) {
                child.setAckMode(parent.getAckMode());
            }
            if (child.getConcurrency() == null) {
                child.setConcurrency(parent.getConcurrency());
            }
            if (child.getPollTimeout() == null) {
                child.setPollTimeout(parent.getPollTimeout());
            }
            if (child.getNoPollThreshold() == null) {
                child.setNoPollThreshold(parent.getNoPollThreshold());
            }
            if (child.getAckCount() == null) {
                child.setAckCount(parent.getAckCount());
            }
            if (child.getAckTime() == null) {
                child.setAckTime(parent.getAckTime());
            }
            String idleBetweenPolls = environment.getProperty(prefix + ".idle-between-polls");
            if (idleBetweenPolls == null) {
                child.setIdleBetweenPolls(parent.getIdleBetweenPolls());
            }
            if (child.getIdleEventInterval() == null) {
                child.setIdleEventInterval(parent.getIdleEventInterval());
            }
            if (child.getMonitorInterval() == null) {
                child.setMonitorInterval(parent.getMonitorInterval());
            }
            if (child.getLogContainerConfig() == null) {
                child.setLogContainerConfig(parent.getLogContainerConfig());
            }
            if (ClassUtils.hasMethod(Listener.class, "isOnlyLogRecordMetadata") &&
                    ClassUtils.hasMethod(Listener.class, "setOnlyLogRecordMetadata")) {
                String onlyLogRecordMetadata = environment.getProperty(prefix + ".only-log-record-metadata");
                if (onlyLogRecordMetadata == null) {
                    child.setOnlyLogRecordMetadata(parent.isOnlyLogRecordMetadata());
                }
            }
            String missingTopicsFatal = environment.getProperty(prefix + ".missing-topics-fatal");
            if (missingTopicsFatal == null) {
                child.setMissingTopicsFatal(parent.isMissingTopicsFatal());
            }
        }

        public void inheritAdmin(Admin child, Admin parent, Environment environment, String prefix) {
            inheritSsl(child.getSsl(), parent.getSsl());
            inheritSecurity(child.getSecurity(), parent.getSecurity());
            inheritProperties(child.getProperties(), parent.getProperties());
            String failFast = environment.getProperty(prefix + ".fail-fast");
            if (failFast == null) {
                child.setFailFast(parent.isFailFast());
            }
        }

        public void inheritStreams(Streams child, Streams parent, Environment environment, String prefix) {
            inheritSsl(child.getSsl(), parent.getSsl());
            inheritSecurity(child.getSecurity(), parent.getSecurity());
            inheritCleanup(child.getCleanup(), parent.getCleanup(), environment, prefix + ".cleanup");
            if (child.getApplicationId() == null) {
                child.setApplicationId(parent.getApplicationId());
            }
            String autoStartup = environment.getProperty(prefix + ".auto-startup");
            if (autoStartup == null) {
                child.setAutoStartup(parent.isAutoStartup());
            }
            if (child.getCacheMaxSizeBuffering() == null) {
                child.setCacheMaxSizeBuffering(parent.getCacheMaxSizeBuffering());
            }
            if (child.getReplicationFactor() == null) {
                child.setReplicationFactor(parent.getReplicationFactor());
            }
            if (child.getStateDir() == null) {
                child.setStateDir(parent.getStateDir());
            }
            inheritProperties(child.getProperties(), parent.getProperties());
        }

        public void inheritSsl(Ssl child, Ssl parent) {
            if (child.getKeyPassword() == null) {
                child.setKeyPassword(parent.getKeyPassword());
            }
            if (ClassUtils.hasMethod(Ssl.class, "getKeyStoreCertificateChain") &&
                    ClassUtils.hasMethod(Ssl.class, "setKeyStoreCertificateChain")) {
                if (child.getKeyStoreCertificateChain() == null) {
                    child.setKeyStoreCertificateChain(parent.getKeyStoreCertificateChain());
                }
            }
            if (ClassUtils.hasMethod(Ssl.class, "getKeyStoreKey") &&
                    ClassUtils.hasMethod(Ssl.class, "setKeyStoreKey")) {
                if (child.getKeyStoreKey() == null) {
                    child.setKeyStoreKey(parent.getKeyStoreKey());
                }
            }
            if (child.getKeyStoreLocation() == null) {
                child.setKeyStoreLocation(parent.getKeyStoreLocation());
            }
            if (child.getKeyStorePassword() == null) {
                child.setKeyStorePassword(parent.getKeyStorePassword());
            }
            if (child.getKeyStoreType() == null) {
                child.setKeyStoreType(parent.getKeyStoreType());
            }
            if (ClassUtils.hasMethod(Ssl.class, "getTrustStoreCertificates") &&
                    ClassUtils.hasMethod(Ssl.class, "setTrustStoreCertificates")) {
                if (child.getTrustStoreCertificates() == null) {
                    child.setTrustStoreCertificates(parent.getTrustStoreCertificates());
                }
            }
            if (child.getTrustStoreLocation() == null) {
                child.setTrustStoreLocation(parent.getTrustStoreLocation());
            }
            if (child.getTrustStorePassword() == null) {
                child.setTrustStorePassword(parent.getTrustStorePassword());
            }
            if (child.getTrustStoreType() == null) {
                child.setTrustStoreType(parent.getTrustStoreType());
            }
            if (child.getProtocol() == null) {
                child.setProtocol(parent.getProtocol());
            }
        }

        public void inheritTemplate(Template child, Template parent) {
            if (child.getDefaultTopic() == null) {
                child.setDefaultTopic(parent.getDefaultTopic());
            }
        }

        public void inheritSecurity(Security child, Security parent) {
            if (child.getProtocol() == null) {
                child.setProtocol(parent.getProtocol());
            }
        }

        public void inheritCleanup(Cleanup child, Cleanup parent, Environment environment, String prefix) {
            String onStartup = environment.getProperty(prefix + ".on-startup");
            if (onStartup == null) {
                child.setOnStartup(parent.isOnStartup());
            }
            String onShutdown = environment.getProperty(prefix + ".on-shutdown");
            if (onShutdown == null) {
                child.setOnShutdown(parent.isOnShutdown());
            }
        }

        public void inheritExtended(EventOperator.PropertyConfig child, EventOperator.PropertyConfig parent) {
            inheritProperties(child.getMetadata(), parent.getMetadata());
            if (child.getEncoder() == null) {
                child.setEncoder(parent.getEncoder());
            }
            if (child.getDecoder() == null) {
                child.setDecoder(parent.getDecoder());
            }
            if (child.getErrorHandler() == null) {
                child.setErrorHandler(parent.getErrorHandler());
            }
            if (child.getPublisher() == null) {
                child.setPublisher(parent.getPublisher());
            }
            if (child.getSubscriber() == null) {
                child.setSubscriber(parent.getSubscriber());
            }
        }
    }
}
