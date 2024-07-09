package com.github.linyuzai.concept.sample.plugin.v2.bean;

import com.github.linyuzai.concept.sample.plugin.CustomPlugin;
import com.github.linyuzai.plugin.autoconfigure.bean.BeanExtractor;
import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SampleBeanListExtractor extends BeanExtractor<List<? extends CustomPlugin>> {

    @Override
    public void onExtract(List<? extends CustomPlugin> plugins, PluginContext context) {
        log.info("Bean List => {}", plugins);
    }
}
