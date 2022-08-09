package com.github.linyuzai.event.rabbitmq.inherit;

import com.github.linyuzai.event.core.inherit.InheritHelper;
import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.time.Duration;

@Getter
@AllArgsConstructor
public class ReflectionRabbitInheritHandler extends InheritHelper implements RabbitInheritHandler {

    private final Environment environment;

    @Override
    public void inherit(RabbitEventProperties properties) {
        inherit(properties, RabbitProperties.class, "concept.event.rabbitmq.endpoints");
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
        return clazz == Duration.class;
    }
}
