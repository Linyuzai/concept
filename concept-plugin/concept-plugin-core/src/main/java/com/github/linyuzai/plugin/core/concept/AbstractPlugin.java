package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.DefaultPluginContext;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.read.PluginReadable;
import com.github.linyuzai.plugin.core.read.PluginReader;
import com.github.linyuzai.plugin.core.read.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public abstract class AbstractPlugin implements Plugin {

    private final Collection<PluginReader> readers = new ArrayList<>();

    @Setter
    private PluginConcept concept;

    public void addReader(PluginReader reader) {
        this.readers.add(reader);
    }

    public void removeReader(PluginReader reader) {
        this.readers.remove(reader);
    }

    public Collection<PluginReader> getReaders(Class<?> readable) {
        return readers.stream()
                .filter(it -> it.support(readable))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(Class<T> type, Object key) {
        for (PluginReader reader : getReaders(type)) {
            Object read = reader.read(key, createReadContent());
            if (read != null) {
                return (T) read;
            }
        }
        return null;
    }

    @Override
    public PluginMetadata getMetadata() {
        return read(PluginMetadata.class, null);
    }

    protected PluginContext createReadContent() {
        return new DefaultPluginContext(null);
    }

    @Override
    public void prepare(PluginContext context) {
        PluginTree.Node node = context.get(PluginTree.Node.class);
        Collection<PluginEntry> entries = collectEntries(context);
        for (PluginEntry entry : entries) {
            Plugin subPlugin = getConcept().create(entry);
            if (subPlugin == null) {
                node.create(entry.getName(), entry, this);
            } else {
                PluginTree.Node subTree = node.create(entry.getName(), subPlugin, this);
                PluginContext subContext = context.createSubContext();
                subContext.initialize();
                subContext.set(Plugin.class, subPlugin);
                subContext.set(PluginTree.Node.class, subTree);
                subPlugin.prepare(subContext);
                subContext.destroy();
            }
        }
        onPrepare(context);
    }

    @Override
    public void release(PluginContext context) {
        PluginTree.Node node = context.get(PluginTree.Node.class);
        for (PluginTree.Node child : node.getChildren()) {
            PluginContext subContext = context.createSubContext();
            subContext.set(PluginTree.Node.class, child);
            subContext.set(Plugin.class, child.getPlugin());
            child.getPlugin().release(subContext);
            subContext.destroy();
        }
        for (PluginReader reader : readers) {
            try {
                reader.close();
            } catch (IOException e) {
                //TODO
            }
        }
        onRelease(context);
    }

    public abstract Collection<PluginEntry> collectEntries(PluginContext context);

    public void onPrepare(PluginContext context) {

    }

    public void onRelease(PluginContext context) {

    }
}
