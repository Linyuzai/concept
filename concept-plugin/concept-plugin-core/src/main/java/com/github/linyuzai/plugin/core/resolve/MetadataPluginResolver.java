package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.read.MetadataReader;
import com.github.linyuzai.plugin.core.read.PropertiesMetadataReader;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.Properties;

@Getter
@Setter
public class MetadataPluginResolver implements PluginResolver {

    private String metadataFilename = "plugin.properties";

    @SneakyThrows
    @Override
    public void resolve(PluginContext context) {
        PluginTree tree = context.get(PluginTree.class);
        tree.getTransformer()
                .create(this)
                .parameter(tree.getRoot())
                .transform(node -> node.<String>filter(name -> {
                    if (metadataFilename.equals(name)) {
                        Plugin plugin = node.getPlugin();
                        readMetadata(plugin);
                        return false;
                    } else {
                        return true;
                    }
                }))
                .resultId(Plugin.PATH_NAME);

    }

    @SneakyThrows
    private void readMetadata(Plugin plugin) {
        try (InputStream is = plugin.read(null, metadataFilename)) {
            Properties properties = new Properties();
            properties.load(is);
            MetadataReader reader = new PropertiesMetadataReader(properties);
            plugin.addReaders(reader);
        }
    }

    @Override
    public boolean support(PluginContext context) {
        return true;
    }
}
