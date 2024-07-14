package com.github.linyuzai.plugin.zip.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.SubPluginFactory;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import com.github.linyuzai.plugin.zip.concept.ZipStreamPlugin;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.zip.ZipInputStream;

/**
 * zip文件子插件工厂
 * <p>
 * 用于解析嵌套的zip文件
 */
public class ZipSubPluginFactory extends SubPluginFactory {

    private static final String SEPARATOR = "!/";

    @SneakyThrows
    @Override
    protected Plugin doCreate(Plugin.Entry entry, PluginMetadata metadata, PluginContext context, PluginConcept concept) {
        if (supportEntry(entry, context)) {
            Object id = entry.getId();
            if (id instanceof URL) {
                URL url = new URL((URL) id, entry.getName() + SEPARATOR);
                return createFromEntry(entry, url, context);
            }
        }
        return null;
    }

    /**
     * 根据条目创建插件
     */
    @SneakyThrows
    protected Plugin createFromEntry(Plugin.Entry entry, URL url, PluginContext context) {
        ZipInputStream zis = new ZipInputStream(entry.getContent().getInputStream());
        return createSubPlugin(zis, url, entry);
    }

    /**
     * 创建子插件
     */
    protected ZipPlugin createSubPlugin(ZipInputStream zis, URL url, Plugin.Entry parent) {
        return new ZipStreamPlugin(zis, url, parent);
    }

    /**
     * 支持 .zip 后缀的条目
     */
    protected boolean supportEntry(Plugin.Entry entry, PluginContext context) {
        return entry.getName().endsWith(".zip");
    }
}
