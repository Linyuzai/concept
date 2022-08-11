package com.github.linyuzai.event.kafka.inherit;

import com.github.linyuzai.event.core.inherit.AbstractInheritHandler;
import com.github.linyuzai.event.kafka.properties.KafkaEventProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.util.unit.DataSize;

import java.lang.reflect.Field;
import java.time.Duration;

@Getter
@AllArgsConstructor
public class ReflectionKafkaInheritHandler extends AbstractInheritHandler<KafkaEventProperties>
        implements KafkaInheritHandler {

    private final Environment environment;

    @Override
    public Class<?> getRootClass() {
        return KafkaProperties.class;
    }

    @Override
    public String getRootPrefix() {
        return "concept.event.kafka.endpoints";
    }

    @Override
    public boolean needInherit(Field field) {
        String name = field.getName();
        return !name.equals("bootstrapServers") && !name.equals("clientId");
    }

    @Override
    public boolean isValueType(Class<?> clazz) {
        return super.isValueType(clazz) &&
                (clazz == Duration.class || clazz == DataSize.class || clazz == Resource.class);
    }

    @Override
    public boolean hasCustomValue(String key) {
        return environment.getProperty(key) != null;
    }
}
