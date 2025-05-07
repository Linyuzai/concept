package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.AbstractPluginEntry;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.util.PluginUtils;
import lombok.Getter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * zip流插件条目
 */
public class ZipStreamPluginEntry extends AbstractPluginEntry implements ZipPluginEntry {

    protected final URL url;

    /**
     * 父条目
     */
    @Getter
    protected final Supplier<InputStream> supplier;

    /**
     * 数据引用
     */
    protected volatile Reference<byte[]> reference;

    public ZipStreamPluginEntry(String name,
                                Plugin plugin,
                                URL url,
                                Supplier<InputStream> supplier,
                                byte[] bytes) {
        super(name, plugin);
        this.url = url;
        this.supplier = supplier;
        if (isDirectory()) {
            return;
        }
        this.reference = createReference(bytes);
    }

    @Override
    public Object getId() {
        return url;
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public Plugin.Content getContent() {
        if (isDirectory()) {
            return null;
        }
        return new ReferenceContent();
    }

    /**
     * 创建引用
     */
    protected Reference<byte[]> createReference(byte[] bytes) {
        return new SoftReference<>(bytes);
    }

    /**
     * 引用内容
     */
    public class ReferenceContent implements Plugin.Content {

        @Override
        public InputStream getInputStream() throws IOException {
            byte[] bytes;
            bytes = reference.get();
            if (bytes != null) {
                return new ByteArrayInputStream(bytes);
            }
            synchronized (ZipStreamPluginEntry.this) {
                bytes = reference.get();
                if (bytes != null) {
                    return new ByteArrayInputStream(bytes);
                }
                try (InputStream is = supplier.get();
                     ZipInputStream zis = new ZipInputStream(is)) {
                    ZipEntry entry;
                    while ((entry = zis.getNextEntry()) != null) {
                        if (Objects.equals(getName(), entry.getName())) {
                            byte[] read = PluginUtils.read(zis);
                            reference = createReference(read);
                            return new ByteArrayInputStream(read);
                        }
                    }
                }
                throw new PluginException("Plugin entry not found");
            }
        }
    }
}
