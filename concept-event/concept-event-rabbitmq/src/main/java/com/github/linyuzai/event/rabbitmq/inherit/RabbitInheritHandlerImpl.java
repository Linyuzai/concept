package com.github.linyuzai.event.rabbitmq.inherit;

import com.github.linyuzai.event.core.config.AbstractInheritHandler;
import com.github.linyuzai.event.core.config.PropertiesConfig;
import com.github.linyuzai.event.rabbitmq.exception.RabbitEventException;
import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 手动罗列所有的配置项
 * <p>
 * 不同版本会存在配置不存在的异常问题
 */
@Deprecated
@Getter
@AllArgsConstructor
public class RabbitInheritHandlerImpl implements RabbitInheritHandler {

    private final Environment environment;

    @Override
    public void inherit(RabbitEventProperties properties) {
        Map<String, RabbitEventProperties.ExtendedRabbitProperties> endpoints = properties.getEndpoints();
        for (Map.Entry<String, RabbitEventProperties.ExtendedRabbitProperties> entry : endpoints.entrySet()) {
            String name = entry.getKey();
            RabbitEventProperties.ExtendedRabbitProperties rabbitProperties = entry.getValue();
            if (StringUtils.hasText(rabbitProperties.getInherit())) {
                RabbitEventProperties.ExtendedRabbitProperties inherit = endpoints.get(rabbitProperties.getInherit());
                if (inherit == null) {
                    throw new RabbitEventException("Inherit not found: " + rabbitProperties.getInherit());
                }
                inherit(name, rabbitProperties, inherit);
            }
        }
    }

    public void inherit(String name,
                        RabbitEventProperties.ExtendedRabbitProperties child,
                        RabbitEventProperties.ExtendedRabbitProperties parent) {
        String prefix = "concept.event.rabbitmq.endpoints." + name;

        if (child.getPort() == null) {
            child.setPort(parent.getPort());
        }

        String username = environment.getProperty(prefix + ".username");
        if (username == null) {
            child.setUsername(parent.getUsername());
        }

        String password = environment.getProperty(prefix + ".password");
        if (password == null) {
            child.setPassword(parent.getPassword());
        }

        inheritSsl(child.getSsl(), parent.getSsl(), prefix + ".ssl");

        if (child.getVirtualHost() == null) {
            child.setVirtualHost(parent.getVirtualHost());
        }

        if (child.getAddresses() == null) {
            child.setAddresses(parent.getAddresses());
        }

        String addressShuffleMode = environment.getProperty(prefix + ".address-shuffle-mode");
        if (addressShuffleMode == null) {
            child.setAddressShuffleMode(parent.getAddressShuffleMode());
        }

        if (child.getRequestedHeartbeat() == null) {
            child.setRequestedHeartbeat(parent.getRequestedHeartbeat());
        }

        String requestedChannelMax = environment.getProperty(prefix + ".requested-channel-max");
        if (requestedChannelMax == null) {
            child.setRequestedChannelMax(parent.getRequestedChannelMax());
        }

        String publisherReturns = environment.getProperty(prefix + ".publisher-returns");
        if (publisherReturns == null) {
            child.setPublisherReturns(parent.isPublisherReturns());
        }

        if (child.getPublisherConfirmType() == null) {
            child.setPublisherConfirmType(parent.getPublisherConfirmType());
        }

        if (child.getConnectionTimeout() == null) {
            child.setConnectionTimeout(parent.getConnectionTimeout());
        }

        String channelRpcTimeout = environment.getProperty(prefix + ".channel-rpc-timeout");
        if (channelRpcTimeout == null) {
            child.setChannelRpcTimeout(parent.getChannelRpcTimeout());
        }
        inheritCache(child.getCache(), parent.getCache(), prefix + "cache");
        inheritListener(child.getListener(), parent.getListener(), prefix + ".listener");
        inheritTemplate(child.getTemplate(), parent.getTemplate(), prefix + ".template");
        inheritStream(child.getStream(), parent.getStream(), prefix + ".stream");
        inheritExtended(child, parent);
    }

    public void inheritSsl(RabbitProperties.Ssl child, RabbitProperties.Ssl parent, String prefix) {
        if (child.getEnabled() == null) {
            child.setEnabled(parent.getEnabled());
        }

        if (child.getKeyStore() == null) {
            child.setKeyStore(parent.getKeyStore());
        }

        String keyStoreType = environment.getProperty(prefix + ".key-store-type");
        if (keyStoreType == null) {
            child.setKeyStoreType(parent.getKeyStoreType());
        }

        if (child.getKeyStorePassword() == null) {
            child.setKeyStorePassword(parent.getKeyStorePassword());
        }

        String keyStoreAlgorithm = environment.getProperty(prefix + ".key-store-algorithm");
        if (keyStoreAlgorithm == null) {
            child.setKeyStoreAlgorithm(parent.getKeyStoreAlgorithm());
        }

        if (child.getTrustStore() == null) {
            child.setTrustStore(parent.getTrustStore());
        }

        String trustStoreType = environment.getProperty(prefix + ".trust-store-type");
        if (trustStoreType == null) {
            child.setTrustStoreType(parent.getTrustStoreType());
        }

        if (child.getTrustStorePassword() == null) {
            child.setTrustStorePassword(parent.getTrustStorePassword());
        }

        String trustStoreAlgorithm = environment.getProperty(prefix + ".trust-store-algorithm");
        if (trustStoreAlgorithm == null) {
            child.setKeyStoreAlgorithm(parent.getKeyStoreAlgorithm());
        }

        if (child.getAlgorithm() == null) {
            child.setAlgorithm(parent.getAlgorithm());
        }

        String validateServerCertificate = environment.getProperty(prefix + ".validate-server-certificate");
        if (validateServerCertificate == null) {
            child.setValidateServerCertificate(parent.isValidateServerCertificate());
        }

        String verifyHostname = environment.getProperty(prefix + ".verify-hostname");
        if (verifyHostname == null) {
            child.setVerifyHostname(parent.getVerifyHostname());
        }
    }

    public void inheritCache(RabbitProperties.Cache child, RabbitProperties.Cache parent, String prefix) {
        inheritCacheChannel(child.getChannel(), parent.getChannel());
        inheritCacheConnection(child.getConnection(), parent.getConnection(), prefix + ".connection");
    }

    public void inheritCacheChannel(RabbitProperties.Cache.Channel child, RabbitProperties.Cache.Channel parent) {
        if (child.getSize() == null) {
            child.setSize(parent.getSize());
        }
        if (child.getCheckoutTimeout() == null) {
            child.setCheckoutTimeout(parent.getCheckoutTimeout());
        }
    }

    public void inheritCacheConnection(RabbitProperties.Cache.Connection child,
                                       RabbitProperties.Cache.Connection parent,
                                       String prefix) {
        String mode = environment.getProperty(prefix + ".mode");
        if (mode == null) {
            child.setMode(parent.getMode());
        }
        if (child.getSize() == null) {
            child.setSize(parent.getSize());
        }
    }

    public void inheritListener(RabbitProperties.Listener child, RabbitProperties.Listener parent, String prefix) {
        String type = environment.getProperty(prefix + ".type");
        if (type == null) {
            child.setType(parent.getType());
        }
        inheritSimpleContainer(child.getSimple(), parent.getSimple(), prefix + ".simple");
        inheritDirectContainer(child.getDirect(), parent.getDirect(), prefix + ".direct");
        inheritStreamContainer(child.getStream(), parent.getStream(), prefix + ".stream");
    }

    public void inheritSimpleContainer(RabbitProperties.SimpleContainer child,
                                       RabbitProperties.SimpleContainer parent,
                                       String prefix) {
        inheritAmqpContainer(child, parent, prefix);

        if (child.getConcurrency() == null) {
            child.setConcurrency(parent.getConcurrency());
        }

        if (child.getMaxConcurrency() == null) {
            child.setMaxConcurrency(parent.getMaxConcurrency());
        }

        if (child.getBatchSize() == null) {
            child.setBatchSize(parent.getBatchSize());
        }

        String missingQueuesFatal = environment.getProperty(prefix + ".missing-queues-fatal");
        if (missingQueuesFatal == null) {
            child.setMissingQueuesFatal(parent.isMissingQueuesFatal());
        }

        String consumerBatchEnabled = environment.getProperty(prefix + ".consumer-batch-enabled");
        if (consumerBatchEnabled == null) {
            child.setConsumerBatchEnabled(parent.isConsumerBatchEnabled());
        }
    }

    public void inheritDirectContainer(RabbitProperties.DirectContainer child,
                                       RabbitProperties.DirectContainer parent,
                                       String prefix) {
        inheritAmqpContainer(child, parent, prefix);

        if (child.getConsumersPerQueue() == null) {
            child.setConsumersPerQueue(parent.getConsumersPerQueue());
        }

        String missingQueuesFatal = environment.getProperty(prefix + ".missing-queues-fatal");
        if (missingQueuesFatal == null) {
            child.setMissingQueuesFatal(parent.isMissingQueuesFatal());
        }
    }

    public void inheritStreamContainer(RabbitProperties.StreamContainer child,
                                       RabbitProperties.StreamContainer parent,
                                       String prefix) {
        inheritBaseContainer(child, parent, prefix);

        String nativeListener = environment.getProperty(prefix + ".native-listener");
        if (nativeListener == null) {
            child.setNativeListener(parent.isNativeListener());
        }
    }

    public void inheritAmqpContainer(RabbitProperties.AmqpContainer child,
                                     RabbitProperties.AmqpContainer parent,
                                     String prefix) {
        inheritBaseContainer(child, parent, prefix);

        if (child.getAcknowledgeMode() == null) {
            child.setAcknowledgeMode(parent.getAcknowledgeMode());
        }

        if (child.getPrefetch() == null) {
            child.setPrefetch(parent.getPrefetch());
        }

        if (child.getDefaultRequeueRejected() == null) {
            child.setDefaultRequeueRejected(parent.getDefaultRequeueRejected());
        }

        if (child.getIdleEventInterval() == null) {
            child.setIdleEventInterval(parent.getIdleEventInterval());
        }

        String deBatchingEnabled = environment.getProperty(prefix + ".de-batching-enabled");
        if (deBatchingEnabled == null) {
            child.setDeBatchingEnabled(parent.isDeBatchingEnabled());
        }

        inheritListenerRetry(child.getRetry(), parent.getRetry(), prefix + ".retry");
    }

    public void inheritListenerRetry(RabbitProperties.ListenerRetry child,
                                     RabbitProperties.ListenerRetry parent,
                                     String prefix) {
        inheritRetry(child, parent, prefix);
        String stateless = environment.getProperty(prefix + ".stateless");
        if (stateless == null) {
            child.setStateless(parent.isStateless());
        }
    }

    public void inheritBaseContainer(RabbitProperties.BaseContainer child,
                                     RabbitProperties.BaseContainer parent,
                                     String prefix) {
        String autoStartup = environment.getProperty(prefix + ".auto-startup");
        if (autoStartup == null) {
            child.setAutoStartup(parent.isAutoStartup());
        }
    }

    public void inheritTemplate(RabbitProperties.Template child,
                                RabbitProperties.Template parent,
                                String prefix) {
        inheritRetry(child.getRetry(), parent.getRetry(), prefix + ".retry");

        if (child.getMandatory() == null) {
            child.setMandatory(parent.getMandatory());
        }

        if (child.getReceiveTimeout() == null) {
            child.setReceiveTimeout(parent.getReceiveTimeout());
        }

        if (child.getReplyTimeout() == null) {
            child.setReplyTimeout(parent.getReplyTimeout());
        }

        String exchange = environment.getProperty(prefix + ".exchange");
        if (exchange == null) {
            child.setExchange(parent.getExchange());
        }

        String routingKey = environment.getProperty(prefix + ".routing-key");
        if (routingKey == null) {
            child.setRoutingKey(parent.getRoutingKey());
        }

        if (child.getDefaultReceiveQueue() == null) {
            child.setDefaultReceiveQueue(parent.getDefaultReceiveQueue());
        }
    }

    public void inheritRetry(RabbitProperties.Retry child,
                             RabbitProperties.Retry parent,
                             String prefix) {
        String enabled = environment.getProperty(prefix + ".enabled");
        if (enabled == null) {
            child.setEnabled(parent.isEnabled());
        }

        String maxAttempts = environment.getProperty(prefix + ".max-attempts");
        if (maxAttempts == null) {
            child.setMaxAttempts(parent.getMaxAttempts());
        }

        String initialInterval = environment.getProperty(prefix + ".initial-interval");
        if (initialInterval == null) {
            child.setInitialInterval(parent.getInitialInterval());
        }

        String multiplier = environment.getProperty(prefix + ".multiplier");
        if (multiplier == null) {
            child.setMultiplier(parent.getMultiplier());
        }

        String maxInterval = environment.getProperty(prefix + ".max-interval");
        if (maxInterval == null) {
            child.setMaxInterval(parent.getMaxInterval());
        }
    }

    public void inheritStream(RabbitProperties.Stream child, RabbitProperties.Stream parent, String prefix) {

        String port = environment.getProperty(".port");
        if (port == null) {
            child.setPort(parent.getPort());
        }

        if (child.getUsername() == null) {
            child.setUsername(parent.getUsername());
        }

        if (child.getPassword() == null) {
            child.setPassword(parent.getPassword());
        }
    }

    public void inheritExtended(PropertiesConfig child, PropertiesConfig parent) {
        AbstractInheritHandler.inherit(child, parent);
    }
}
