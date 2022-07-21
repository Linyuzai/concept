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
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ConfigurationProperties("concept.event.kafka")
public class KafkaEventProperties implements EventOperator.PropertyConfig {

    private boolean enabled = true;

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
            inheritProperties(getProperties(), parent.getProperties());
            inheritConsumer(getConsumer(), parent.getConsumer());
            inheritProducer(getProducer(), parent.getProducer());
            inheritListener(getListener(), parent.getListener());
            inheritAdmin(getAdmin(), parent.getAdmin());
            inheritStreams(getStreams(), parent.getStreams());
            inheritSsl(getSsl(), parent.getSsl());
            //jaas好像是全局的，可以配置在spring.kafka中
            inheritTemplate(getTemplate(), parent.getTemplate());
            inheritSecurity(getSecurity(), parent.getSecurity());
        }

        public void inheritProperties(Map<String, String> child, Map<String, String> parent) {
            for (Map.Entry<String, String> entry : parent.entrySet()) {
                String key = entry.getKey();
                if (child.containsKey(key)) {
                    continue;
                }
                child.put(key, entry.getValue());
            }
        }

        public void inheritConsumer(Consumer child, Consumer parent) {
            inheritSsl(child.getSsl(), parent.getSsl());
            inheritSecurity(child.getSecurity(), parent.getSecurity());
        }

        public void inheritProducer(Producer child, Producer parent) {

        }

        public void inheritListener(Listener child, Listener parent) {

        }

        public void inheritAdmin(Admin child, Admin parent) {

        }

        public void inheritStreams(Streams child, Streams parent) {

        }

        public void inheritSsl(Ssl child, Ssl parent) {
            if (child.getKeyPassword() == null) {
                child.setKeyPassword(parent.getKeyPassword());
            }
            if (child.getKeyStoreCertificateChain() == null) {
                child.setKeyStoreCertificateChain(parent.getKeyStoreCertificateChain());
            }
            if (child.getKeyStoreKey() == null) {
                child.setKeyStoreKey(parent.getKeyStoreKey());
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
            if (child.getTrustStoreCertificates() == null) {
                child.setTrustStoreCertificates(parent.getTrustStoreCertificates());
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

        }

        public void inheritSecurity(Security child, Security parent) {

        }
    }
}
