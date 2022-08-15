package com.github.linyuzai.event.rabbitmq.inherit;

import com.github.linyuzai.event.core.config.AbstractConfigInheritHandler;
import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.time.Duration;

/**
 * 基于反射的 RabbitMQ 配置继承处理器
 */
@Getter
@AllArgsConstructor
public class ReflectionRabbitConfigInheritHandler extends AbstractConfigInheritHandler<RabbitEventProperties>
        implements RabbitConfigInheritHandler {

    private final Environment environment;

    @Override
    public Class<?> getRootClass() {
        return RabbitProperties.class;
    }

    @Override
    public String getRootPrefix() {
        return "concept.event.rabbitmq.endpoints";
    }

    @Override
    public boolean needInherit(Field field) {
        String name = field.getName();
        return !name.equals("parsedAddresses") && !name.equals("host");
    }

    @Override
    public boolean hasCustomValue(String key) {
        return environment.getProperty(key) != null;
    }

    @Override
    public boolean isValueType(Class<?> clazz) {
        return super.isValueType(clazz) || clazz == Duration.class;
    }
}
