package com.github.linyuzai.event.autoconfigure.bus;

import com.github.linyuzai.event.core.config.AbstractPropertiesConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 事件总线配置属性
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "concept.event.bus")
public class EventBusProperties extends AbstractPropertiesConfig {

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * 事件总线使用的引擎
     */
    private String engine;

    /**
     * 事件总线使用的端点
     */
    private String endpoint;
}
