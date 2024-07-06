package com.github.linyuzai.concept.sample.plugin.v2;

import com.github.linyuzai.concept.sample.plugin.CustomPlugin;
import com.github.linyuzai.plugin.autoconfigure.bean.BeanExtractor;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.jar.handle.extract.match.PluginClassName;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SampleBeanExtractor extends BeanExtractor<List<? extends CustomPlugin>> {

    @Override
    public void onExtract(@PluginClassName("com.example.plugin.**Plugin") List<? extends CustomPlugin> plugins, PluginContext context) {
        System.out.println(plugins);
    }
}
