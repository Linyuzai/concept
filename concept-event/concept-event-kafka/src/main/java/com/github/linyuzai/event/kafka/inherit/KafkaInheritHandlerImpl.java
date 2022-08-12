package com.github.linyuzai.event.kafka.inherit;

import com.github.linyuzai.event.core.config.PropertiesConfig;
import com.github.linyuzai.event.core.config.AbstractInheritHandler;
import com.github.linyuzai.event.kafka.exception.KafkaEventException;
import com.github.linyuzai.event.kafka.properties.KafkaEventProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@Deprecated
@Getter
@AllArgsConstructor
public class KafkaInheritHandlerImpl implements KafkaInheritHandler {

    private final Environment environment;

    @Override
    public void inherit(KafkaEventProperties properties) {
        Map<String, KafkaEventProperties.ExtendedKafkaProperties> endpoints = properties.getEndpoints();
        for (Map.Entry<String, KafkaEventProperties.ExtendedKafkaProperties> entry : endpoints.entrySet()) {
            String name = entry.getKey();
            KafkaEventProperties.ExtendedKafkaProperties kafkaProperties = entry.getValue();
            if (StringUtils.hasText(kafkaProperties.getInherit())) {
                KafkaEventProperties.ExtendedKafkaProperties inherit = endpoints.get(kafkaProperties.getInherit());
                if (inherit == null) {
                    throw new KafkaEventException("Inherit not found: " + kafkaProperties.getInherit());
                }
                inherit(name, kafkaProperties, inherit);
            }
        }
    }

    public void inherit(String name,
                        KafkaEventProperties.ExtendedKafkaProperties child,
                        KafkaEventProperties.ExtendedKafkaProperties parent) {
        String prefix = "concept.event.kafka.endpoints." + name;
        inheritProperties(child.getProperties(), parent.getProperties());
        inheritConsumer(child.getConsumer(), parent.getConsumer(), prefix + ".consumer");
        inheritProducer(child.getProducer(), parent.getProducer(), prefix + ".producer");
        inheritListener(child.getListener(), parent.getListener(), prefix + ".listener");
        inheritAdmin(child.getAdmin(), parent.getAdmin(), prefix + ".admin");
        inheritStreams(child.getStreams(), parent.getStreams(), prefix + ".streams");
        inheritSsl(child.getSsl(), parent.getSsl());
        //jaas好像是全局的，可以配置在spring.kafka中
        inheritTemplate(child.getTemplate(), parent.getTemplate());
        inheritSecurity(child.getSecurity(), parent.getSecurity());
        inheritExtended(child, parent);
    }

    public <K, V> void inheritProperties(Map<K, V> child, Map<K, V> parent) {
        AbstractInheritHandler.inheritMap(child, parent);
    }

    public void inheritConsumer(KafkaProperties.Consumer child, KafkaProperties.Consumer parent, String prefix) {
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

    public void inheritProducer(KafkaProperties.Producer child, KafkaProperties.Producer parent, String prefix) {
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

    public void inheritListener(KafkaProperties.Listener child, KafkaProperties.Listener parent, String prefix) {
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
        if (ClassUtils.hasMethod(KafkaProperties.Listener.class, "isOnlyLogRecordMetadata") &&
                ClassUtils.hasMethod(KafkaProperties.Listener.class, "setOnlyLogRecordMetadata")) {
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

    public void inheritAdmin(KafkaProperties.Admin child, KafkaProperties.Admin parent, String prefix) {
        inheritSsl(child.getSsl(), parent.getSsl());
        inheritSecurity(child.getSecurity(), parent.getSecurity());
        inheritProperties(child.getProperties(), parent.getProperties());
        String failFast = environment.getProperty(prefix + ".fail-fast");
        if (failFast == null) {
            child.setFailFast(parent.isFailFast());
        }
    }

    public void inheritStreams(KafkaProperties.Streams child, KafkaProperties.Streams parent, String prefix) {
        inheritSsl(child.getSsl(), parent.getSsl());
        inheritSecurity(child.getSecurity(), parent.getSecurity());
        inheritCleanup(child.getCleanup(), parent.getCleanup(), prefix + ".cleanup");
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

    public void inheritSsl(KafkaProperties.Ssl child, KafkaProperties.Ssl parent) {
        if (child.getKeyPassword() == null) {
            child.setKeyPassword(parent.getKeyPassword());
        }
        if (ClassUtils.hasMethod(KafkaProperties.Ssl.class, "getKeyStoreCertificateChain") &&
                ClassUtils.hasMethod(KafkaProperties.Ssl.class, "setKeyStoreCertificateChain")) {
            if (child.getKeyStoreCertificateChain() == null) {
                child.setKeyStoreCertificateChain(parent.getKeyStoreCertificateChain());
            }
        }
        if (ClassUtils.hasMethod(KafkaProperties.Ssl.class, "getKeyStoreKey") &&
                ClassUtils.hasMethod(KafkaProperties.Ssl.class, "setKeyStoreKey")) {
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
        if (ClassUtils.hasMethod(KafkaProperties.Ssl.class, "getTrustStoreCertificates") &&
                ClassUtils.hasMethod(KafkaProperties.Ssl.class, "setTrustStoreCertificates")) {
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

    public void inheritTemplate(KafkaProperties.Template child, KafkaProperties.Template parent) {
        if (child.getDefaultTopic() == null) {
            child.setDefaultTopic(parent.getDefaultTopic());
        }
    }

    public void inheritSecurity(KafkaProperties.Security child, KafkaProperties.Security parent) {
        if (child.getProtocol() == null) {
            child.setProtocol(parent.getProtocol());
        }
    }

    public void inheritCleanup(KafkaProperties.Cleanup child, KafkaProperties.Cleanup parent, String prefix) {
        String onStartup = environment.getProperty(prefix + ".on-startup");
        if (onStartup == null) {
            child.setOnStartup(parent.isOnStartup());
        }
        String onShutdown = environment.getProperty(prefix + ".on-shutdown");
        if (onShutdown == null) {
            child.setOnShutdown(parent.isOnShutdown());
        }
    }

    public void inheritExtended(PropertiesConfig child, PropertiesConfig parent) {
        AbstractInheritHandler.inherit(child, parent);
    }
}
