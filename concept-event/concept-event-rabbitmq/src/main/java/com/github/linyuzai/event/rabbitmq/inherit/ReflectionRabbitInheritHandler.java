package com.github.linyuzai.event.rabbitmq.inherit;

import com.github.linyuzai.event.core.inherit.AbstractInheritHandler;
import com.github.linyuzai.event.rabbitmq.properties.RabbitEventProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.time.Duration;

@Getter
@AllArgsConstructor
public class ReflectionRabbitInheritHandler extends AbstractInheritHandler<RabbitEventProperties>
        implements RabbitInheritHandler {

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
        return super.isValueType(clazz) && clazz == Duration.class;
    }
}
