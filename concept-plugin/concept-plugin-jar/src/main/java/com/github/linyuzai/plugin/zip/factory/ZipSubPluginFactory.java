package com.github.linyuzai.plugin.zip.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.SubPluginFactory;
import com.github.linyuzai.plugin.zip.concept.ZipStreamPlugin;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.zip.ZipInputStream;

public class ZipSubPluginFactory extends SubPluginFactory {

    private static final String SEPARATOR = "!/";

    @SneakyThrows
    @Override
    public Plugin doCreate(Plugin.Entry entry, PluginContext context) {
        if (supportEntry(entry, context)) {
            Object id = entry.getId();
            if (id instanceof URL) {
                URL url = new URL((URL) id, entry.getName() + SEPARATOR);
                return createFromEntry(entry, url, context);
            }
        }
        return null;
    }

    @SneakyThrows
    protected Plugin createFromEntry(Plugin.Entry entry, URL url, PluginContext context) {
        ZipInputStream zis = new ZipInputStream(entry.getContent().getInputStream());
        return createZipPlugin(zis, url, entry);
    }

    protected ZipStreamPlugin createZipPlugin(ZipInputStream zis, URL url, Plugin.Entry parent) {
        return new ZipStreamPlugin(zis, url, parent);
    }

    protected boolean supportEntry(Plugin.Entry entry, PluginContext context) {
        return entry.getName().endsWith(".zip");
    }
}
