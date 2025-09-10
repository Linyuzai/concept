package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.AbstractPluginEntry;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.storage.PluginStorage;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * zip流插件条目
 */
public class ZipStreamPluginEntry extends AbstractPluginEntry implements ZipPluginEntry {

    @Getter
    protected final String path;

    @Getter
    protected final long size;

    /**
     * 父条目
     */
    @Getter
    protected final Supplier<InputStream> supplier;

    /**
     * 数据引用
     */
    protected volatile Reference<byte[]> reference;

    public ZipStreamPluginEntry(Plugin parent,
                                String name,
                                String path,
                                Supplier<InputStream> supplier,
                                byte[] bytes) {
        super(parent, name);
        this.path = path;
        this.supplier = supplier;
        if (isDirectory()) {
            this.size = 0L;
            return;
        }
        this.size = bytes.length;
        this.reference = createReference(bytes);
    }

    @Nullable
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

        @SneakyThrows
        @Override
        public InputStream getInputStream() {
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
                            byte[] read = PluginStorage.read(zis);
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
