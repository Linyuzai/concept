package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.DefaultPluginContext;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.read.PluginReader;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class AbstractPlugin implements Plugin {

    private final Collection<PluginReader> readers = new CopyOnWriteArrayList<>();

    private final Collection<DestroyListener> destroyListeners = new CopyOnWriteArrayList<>();

    private PluginMetadata metadata;

    private PluginConcept concept;

    @Override
    public void addReader(PluginReader reader) {
        this.readers.add(reader);
    }

    @Override
    public void removeReader(PluginReader reader) {
        this.readers.remove(reader);
    }

    @Override
    public void addDestroyListener(DestroyListener listener) {
        this.destroyListeners.add(listener);
    }

    @Override
    public void removeDestroyListener(DestroyListener listener) {
        this.destroyListeners.remove(listener);
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
    public void prepare(PluginContext context) {
        PluginTree.NodeFactory node = context.get(PluginTree.Node.class);
        collectEntries(context, entry -> {
            PluginConcept concept = context.getConcept();

            PluginContext subContext = context.createSubContext(false);
            subContext.set(PluginConcept.class, concept);
            subContext.initialize();

            Plugin subPlugin = getConcept().create(entry, subContext);
            if (subPlugin == null) {
                node.create(entry.getId(), entry.getName(), entry);
            } else {
                subPlugin.setConcept(concept);
                subPlugin.initialize();

                PluginTree.Node subTree = node.create(entry.getId(), entry.getName(), subPlugin);
                subContext.set(Plugin.class, subPlugin);
                subContext.set(PluginTree.Node.class, subTree);

                subPlugin.prepare(subContext);

                subContext.destroy();
            }
        });
        onPrepare(context);
    }

    @Override
    public void release(PluginContext context) {
        PluginTree.Node node = context.get(PluginTree.Node.class);
        for (PluginTree.Node child : node.getChildren()) {
            if (child.getValue() instanceof Plugin) {
                Plugin subPlugin = (Plugin) child.getValue();
                PluginContext subContext = context.createSubContext(false);
                subContext.initialize();
                subContext.set(PluginTree.Node.class, child);
                subContext.set(Plugin.class, subPlugin);
                subPlugin.release(subContext);
                subContext.destroy();
            }
        }
        onRelease(context);
    }

    @Override
    public void destroy() {
        for (DestroyListener listener : destroyListeners) {
            listener.onDestroy(this);
        }
        for (PluginReader reader : readers) {
            try {
                reader.close();
            } catch (IOException ignore) {
            }
        }
        onDestroy();
    }

    public abstract void collectEntries(PluginContext context, Consumer<Entry> consumer);

    public void onInitialize() {

    }

    public void onDestroy() {

    }

    public void onPrepare(PluginContext context) {

    }

    public void onRelease(PluginContext context) {

    }

    @Override
    public String toString() {
        String name = metadata.property(MetadataProperties.NAME, getId().toString());
        return getClass().getSimpleName() + "(" + name + ")";
    }
}
