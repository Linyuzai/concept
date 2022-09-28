package com.github.linyuzai.event.local.properties;

import com.github.linyuzai.event.core.config.AbstractPropertiesConfig;
import com.github.linyuzai.event.core.config.EndpointConfig;
import com.github.linyuzai.event.core.config.EngineConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 本地事件配置
 */
@Getter
@Setter
@ConfigurationProperties("concept.event.local")
public class LocalEventProperties extends AbstractPropertiesConfig implements EngineConfig {

    /**
     * 是否启用引擎
     */
    private boolean enabled;

    /**
     * 本地事件端点配置
     */
    private Map<String, LocalProperties> endpoints = new LinkedHashMap<>();

    /**
     * 本地配置
     */
    @Getter
    @Setter
    public static class LocalProperties extends AbstractPropertiesConfig implements EndpointConfig {

        /**
         * 是否启用端点
         */
        private boolean enabled = true;

        /**
         * 配置继承的端点名
         */
        private String inherit;
    }
}
