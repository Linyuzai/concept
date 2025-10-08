package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.listener.PluginListener;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.core.sync.SyncSupport;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * 插件抽象类
 */
@Getter
@Setter
public abstract class AbstractPlugin extends SyncSupport implements Plugin {

    private final List<PluginListener> listeners = new ArrayList<>();

    private PluginDefinition definition;

    private PluginMetadata metadata;

    private PluginConcept concept;

    private volatile boolean loaded = false;

    @Override
    public void addListener(@NonNull PluginListener listener) {
        syncWrite(() -> listeners.add(listener));
    }

    @Override
    public void removeListener(@NonNull PluginListener listener) {
        syncWrite(() -> listeners.remove(listener));
    }

    public List<PluginListener> getListeners() {
        return syncRead(() -> Collections.unmodifiableList(listeners));
    }

    /**
     * 解析插件树
     */
    @Override
    public void load(PluginContext context) {
        syncWrite(() -> {
            if (!loaded) {
                for (PluginListener listener : listeners) {
                    listener.onLoad(this, context);
                }
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
        });
    }

    @Override
    public void unload() {
        syncWrite(() -> {
            if (loaded) {
                for (PluginListener listener : listeners) {
                    listener.onUnload(this);
                }
                onUnload();
                loaded = true;
            }
        });
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
