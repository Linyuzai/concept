package com.github.linyuzai.plugin.autoconfigure;

import com.github.linyuzai.plugin.autoconfigure.autoload.ConditionalOnPluginAutoloadEnabled;
import com.github.linyuzai.plugin.autoconfigure.preperties.PluginConceptProperties;
import com.github.linyuzai.plugin.core.autoload.DefaultPluginAutoLoader;
import com.github.linyuzai.plugin.core.autoload.PluginAutoLoader;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.executer.DefaultPluginExecutor;
import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import com.github.linyuzai.plugin.core.storage.PluginStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 插件自动加载配置类
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnPluginAutoloadEnabled
public class PluginAutoloadConfiguration {

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    public PluginExecutor pluginExecutor() {
        return new DefaultPluginExecutor();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean
    public PluginAutoLoader pluginAutoLoader(PluginConcept concept,
                                             PluginExecutor executor,
                                             PluginStorage storage,
                                             PluginConceptProperties properties) {
        return new DefaultPluginAutoLoader(concept, executor, storage,
                properties.getAutoload().getPeriod());
    }
}
