package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * 插件抽象类
 */
@Getter
@Setter
public abstract class AbstractPlugin implements Plugin {

    private final Collection<LoadListener> loadListeners = new ArrayList<>();

    private final Collection<UnloadListener> unloadListeners = new ArrayList<>();

    private PluginDefinition definition;

    private PluginMetadata metadata;

    private PluginConcept concept;

    private boolean loaded = false;

    @Override
    public synchronized void addLoadListener(@NonNull LoadListener listener) {
        loadListeners.add(listener);
    }

    @Override
    public synchronized void removeLoadListener(@NonNull LoadListener listener) {
        loadListeners.remove(listener);
    }

    @Override
    public synchronized void addUnloadListener(@NonNull UnloadListener listener) {
        this.unloadListeners.add(listener);
    }

    @Override
    public synchronized void removeUnloadListener(@NonNull UnloadListener listener) {
        this.unloadListeners.remove(listener);
    }

    /**
     * 解析插件树
     */
    @Override
    public synchronized void load(PluginContext context) {
        if (!loaded) {
            PluginTree.NodeFactory node = context.get(PluginTree.Node.class);
            forEachEntry(context, entry -> {
                PluginConcept concept = context.getConcept();
                //子上下文
                PluginContext subContext = context.createSubContext();
                subContext.set(PluginConcept.class, concept);
                subContext.initialize();
                //子插件
                Plugin subPlugin = getConcept().createPlugin(entry, subContext);
                if (subPlugin == null) {
                    node.create(entry.getPath(), entry.getName(), entry);
                } else {
                    subPlugin.setConcept(concept);
                    //子插件树
                    PluginTree.Node subTree = node.create(entry.getPath(), entry.getName(), subPlugin);
                    subContext.set(Plugin.class, subPlugin);
                    subContext.set(PluginTree.Node.class, subTree);
                    //解析子插件
                    subPlugin.load(subContext);
                    subContext.destroy();
                }
            });
            onLoad(context);
            loaded = true;
        }
    }

    @Override
    public synchronized void unload() {
        if (loaded) {
            for (UnloadListener listener : unloadListeners) {
                listener.onUnload(this);
            }
            onUnload();
            loaded = true;
        }
    }

    /**
     * 遍历条目
     */
    public abstract void forEachEntry(PluginContext context, Consumer<Entry> consumer);


    /**
     * 加载
     */
    public void onLoad(PluginContext context) {

    }

    /**
     * 卸载
     */
    public void onUnload() {

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getDefinition().getPath() + ")";
    }
}
