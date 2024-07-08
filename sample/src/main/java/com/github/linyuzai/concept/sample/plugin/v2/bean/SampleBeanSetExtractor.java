package com.github.linyuzai.concept.sample.plugin.v2.bean;

import com.github.linyuzai.concept.sample.plugin.CustomPlugin;
import com.github.linyuzai.concept.sample.plugin.v2.PluginConfig;
import com.github.linyuzai.plugin.autoconfigure.bean.BeanExtractor;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.jar.handle.extract.match.PluginClassName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class SampleBeanSetExtractor extends BeanExtractor<Set<? extends CustomPlugin>> {

    @Override
    public void onExtract(Set<? extends CustomPlugin> plugins, PluginContext context) {
        log.info("Bean Set => {}", plugins);
    }
}
