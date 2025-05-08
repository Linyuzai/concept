package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class JarPluginFactory implements PluginFactory {

    private String defaultMode = JarPlugin.Mode.STREAM;

    public String getMode(PluginMetadata metadata) {
        JarPlugin.StandardMetadata standard = metadata.asStandard();
        String mode = standard.getJar().getMode();
        return (mode == null || mode.isEmpty()) ? defaultMode : mode;
    }
}
