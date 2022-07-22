package com.github.linyuzai.concept.sample.plugin.v2;

import com.github.linyuzai.concept.sample.plugin.CustomPlugin;
import com.github.linyuzai.plugin.core.extract.OnPluginExtract;
import org.springframework.stereotype.Component;

@EnableJarPluginConcept
@Component
public class JarPluginVersion2 {

    @OnPluginExtract
    private void onPluginExtract(Class<? extends CustomPlugin> plugin) {

    }
}
