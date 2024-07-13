package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.PluginHandlerChain;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Getter
@Setter
public abstract class AbstractPlugin implements Plugin {

    private final Collection<LoadListener> loadListeners = new CopyOnWriteArrayList<>();

    private final Collection<DestroyListener> destroyListeners = new CopyOnWriteArrayList<>();

    private PluginMetadata metadata;

    private PluginConcept concept;

    @Override
    public void addLoadListener(LoadListener listener) {
        this.loadListeners.add(listener);
    }

    @Override
    public void removeLoadListener(LoadListener listener) {
        this.loadListeners.remove(listener);
    }

    @Override
    public void addDestroyListener(DestroyListener listener) {
        this.destroyListeners.add(listener);
    }

    @Override
    public void removeDestroyListener(DestroyListener listener) {
        this.destroyListeners.remove(listener);
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
    public void load(PluginHandlerChain chain, PluginContext context) {
        chain.next(context);
        for (LoadListener listener : loadListeners) {
            listener.onLoad(this);
        }
    }

    @Override
    public void destroy() {
        for (DestroyListener listener : destroyListeners) {
            listener.onDestroy(this);
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getURL() + ")";
    }
}
