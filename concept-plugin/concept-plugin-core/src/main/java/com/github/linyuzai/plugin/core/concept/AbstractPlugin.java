package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.DefaultPluginContext;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.read.PluginReader;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class AbstractPlugin implements Plugin {

    private final Collection<PluginReader> readers = new ArrayList<>();

    private Metadata metadata;

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

    protected PluginContext createReadContent() {
        return new DefaultPluginContext(null);
    }

    @Override
    public void initialize() {
        onInitialize();
    }

    @Override
    public void open(PluginContext context) {
        PluginTree.NodeFactory node = context.get(PluginTree.Node.class);
        collectEntries(context, entry -> {
            Plugin subPlugin = getConcept().create(entry, context);
            if (subPlugin == null) {
                node.create(entry.getId(), entry.getName(), entry);
            } else {
                subPlugin.setConcept(getConcept());
                PluginTree.Node subTree = node.create(subPlugin.getId(), entry.getName(), subPlugin);
                PluginContext subContext = context.createSubContext(false);
                subContext.initialize();
                subContext.set(Plugin.class, subPlugin);
                subContext.set(PluginTree.Node.class, subTree);
                subPlugin.open(subContext);
                subContext.destroy();
            }
        });
        onOpen(context);
    }

    @Override
    public void close(PluginContext context) {
        PluginTree.Node node = context.get(PluginTree.Node.class);
        for (PluginTree.Node child : node.getChildren()) {
            if (child.getValue() instanceof Plugin) {
                Plugin subPlugin = (Plugin) child.getValue();
                PluginContext subContext = context.createSubContext(false);
                subContext.set(PluginTree.Node.class, child);
                subContext.set(Plugin.class, subPlugin);
                subPlugin.close(subContext);
                subContext.destroy();
            }
        }
        onClose(context);
    }

    @Override
    public void destroy() {
        for (PluginReader reader : readers) {
            try {
                reader.close();
            } catch (IOException e) {
                //TODO
            }
        }
        onDestroy();
    }

    public abstract void collectEntries(PluginContext context, Consumer<Entry> consumer);

    public void onInitialize() {

    }

    public void onDestroy() {

    }

    public void onOpen(PluginContext context) {

    }

    public void onClose(PluginContext context) {

    }
}
