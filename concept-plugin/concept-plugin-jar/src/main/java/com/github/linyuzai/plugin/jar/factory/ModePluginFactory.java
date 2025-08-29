package com.github.linyuzai.plugin.jar.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ModePluginFactory implements PluginFactory {

    private String defaultMode = JarPlugin.Mode.STREAM;

    public String getMode(PluginMetadata metadata) {
        Plugin.StandardMetadata standard = metadata.asStandard();
        if (standard instanceof JarPlugin.StandardMetadata) {
            String mode = ((JarPlugin.StandardMetadata) standard).getJar().getMode();
            return (mode == null || mode.isEmpty()) ? defaultMode : mode;
        } else {
            return defaultMode;
        }
    }
}
