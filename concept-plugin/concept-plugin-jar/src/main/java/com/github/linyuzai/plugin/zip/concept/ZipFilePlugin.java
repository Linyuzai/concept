package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.AbstractPlugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.path.PluginPathFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * zip文件插件
 */
@Getter
@RequiredArgsConstructor
public class ZipFilePlugin extends AbstractPlugin implements ZipPlugin {

    protected final ZipFile zipFile;

    @Override
    public void forEachEntry(PluginContext context, Consumer<Entry> consumer) {
        PluginPathFactory pathFactory = context.getConcept().getPathFactory();
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String name = entry.getName();
            String path = pathFactory.create(getDefinition().getPath(), name);
            ZipPluginEntry pluginEntry = createPluginEntry(path, name);
            consumer.accept(pluginEntry);
        }
    }

    /**
     * 创建zip插件条目
     */
    protected ZipPluginEntry createPluginEntry(String path, String name) {
        return new ZipFilePluginEntry(this, name, path, zipFile);
    }

    /**
     * 关闭zip文件
     */
    @Override
    public void onDestroy() {
        try {
            zipFile.close();
        } catch (Throwable ignore) {
        }
    }
}
