package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.PluginHandlerChain;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * 插件抽象类
 */
@Getter
@Setter
public abstract class AbstractPlugin implements Plugin {

    private final Collection<LoadListener> loadListeners = new CopyOnWriteArrayList<>();

    private final Collection<DestroyListener> destroyListeners = new CopyOnWriteArrayList<>();

    private Object source;

    private PluginMetadata metadata;

    private PluginConcept concept;

    private AtomicBoolean destroyed = new AtomicBoolean(false);

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

    /**
     * 解析插件树
     */
    @Override
    public void prepare(PluginContext context) {
        PluginTree.NodeFactory node = context.get(PluginTree.Node.class);
        forEachEntry(context, entry -> {
            PluginConcept concept = context.getConcept();
            //子上下文
            PluginContext subContext = context.createSubContext();
            subContext.set(PluginConcept.class, concept);
            subContext.initialize();
            //子插件
            Plugin subPlugin = getConcept().create(entry, subContext);
            if (subPlugin == null) {
                node.create(entry.getId(), entry.getName(), entry);
            } else {
                subPlugin.setConcept(concept);
                subPlugin.initialize();
                //子插件树
                PluginTree.Node subTree = node.create(entry.getId(), entry.getName(), subPlugin);
                subContext.set(Plugin.class, subPlugin);
                subContext.set(PluginTree.Node.class, subTree);
                //解析子插件
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
        if (destroyed.compareAndSet(false, true)) {
            for (DestroyListener listener : destroyListeners) {
                listener.onDestroy(this);
            }
            onDestroy();
        }
    }

    /**
     * 遍历条目
     */
    public abstract void forEachEntry(PluginContext context, Consumer<Entry> consumer);

    /**
     * 初始化
     */
    public void onInitialize() {

    }

    /**
     * 销毁
     */
    public void onDestroy() {

    }

    /**
     * 准备
     */
    public void onPrepare(PluginContext context) {

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getId() + ")";
    }
}
