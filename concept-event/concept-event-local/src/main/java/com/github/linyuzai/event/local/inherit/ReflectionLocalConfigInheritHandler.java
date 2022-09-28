package com.github.linyuzai.event.local.inherit;

import com.github.linyuzai.event.core.config.AbstractConfigInheritHandler;
import com.github.linyuzai.event.local.properties.LocalEventProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.env.Environment;

/**
 * 基于反射的本地配置继承处理器
 */
@Getter
@AllArgsConstructor
public class ReflectionLocalConfigInheritHandler extends AbstractConfigInheritHandler<LocalEventProperties>
        implements LocalConfigInheritHandler {

    private final Environment environment;

    @Override
    public Class<?> getRootClass() {
        return LocalEventProperties.LocalProperties.class;
    }

    @Override
    public String getRootPrefix() {
        return "concept.event.local.endpoints";
    }

    @Override
    public boolean hasCustomValue(String key) {
        return environment.getProperty(key) != null;
    }
}
