package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.AbstractPlugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * zip文件插件
 */
@RequiredArgsConstructor
public class ZipFilePlugin extends AbstractPlugin implements ZipPlugin {

    /**
     * 插件路径
     */
    @Getter
    protected final ZipFile zipFile;

    protected final URL url;

    @Override
    public Object getId() {
        return url;
    }

    @Override
    public URL getURL() {
        return url;
    }

    @SneakyThrows
    @Override
    public void forEachEntry(PluginContext context, Consumer<Entry> consumer) {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String name = entry.getName();
            URL id = new URL(url, name);
            ZipPluginEntry pluginEntry = createPluginEntry(id, name);
            consumer.accept(pluginEntry);
        }
    }

    /**
     * 创建zip插件条目
     */
    protected ZipPluginEntry createPluginEntry(URL url, String name) {
        return new ZipFilePluginEntry(zipFile, url, name, this);
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
