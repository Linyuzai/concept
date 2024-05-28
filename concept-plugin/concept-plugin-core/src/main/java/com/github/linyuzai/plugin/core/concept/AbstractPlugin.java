package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.read.PluginReader;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public abstract class AbstractPlugin implements Plugin {

    private final Collection<PluginReader> readers = new ArrayList<>();

    @Setter
    private PluginConcept concept;

    @Override
    public void addReaders(PluginReader... readers) {
        this.readers.addAll(Arrays.asList(readers));
    }

    @Override
    public Collection<PluginReader> getReaders(Class<? extends PluginReader> type) {
        return readers.stream()
                .filter(type::isInstance)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(Class<? extends PluginReader> type, String name) {
        for (PluginReader reader : getReaders(type)) {
            Object read = reader.read(name);
            if (read != null) {
                return (T) read;
            }
        }
        return null;
    }

    @Override
    public void prepare(PluginContext context) {
        onPrepare(context);
        PluginTree.Node node = context.get(PluginTree.Node.class);
        Collection<Object> contents = collectContent(context);
        for (Object content : contents) {
            Plugin subPlugin = context.getConcept().create(content);
            if (subPlugin == null) {
                node.create(content, content, this);
            } else {
                PluginTree.Node subTree = node.create(content, subPlugin, this);
                PluginContext subContext = context.createSubContext();
                subContext.initialize();
                subContext.set(Plugin.class, subPlugin);
                subContext.set(PluginTree.class, subTree);
                subPlugin.prepare(subContext);
                subContext.destroy();
            }
        }
    }

    @Override
    public void release(PluginContext context) {
        onRelease(context);
        PluginTree.Node node = context.get(PluginTree.Node.class);
        for (PluginTree.Node child : node.getChildren()) {
            PluginContext subContext = context.createSubContext();
            subContext.set(PluginTree.class, child);
            subContext.set(Plugin.class, child.getPlugin());
            child.getPlugin().release(subContext);
            subContext.destroy();
        }
    }

    public abstract Collection<Object> collectContent(PluginContext context);

    public void onPrepare(PluginContext context) {

    }

    public void onRelease(PluginContext context) {

    }
}
