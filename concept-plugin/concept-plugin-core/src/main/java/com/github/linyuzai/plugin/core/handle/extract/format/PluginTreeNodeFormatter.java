package com.github.linyuzai.plugin.core.handle.extract.format;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于
 */
public abstract class PluginTreeNodeFormatter<T> extends AbstractPluginFormatter<PluginTree.Node, T> {

    @Override
    public T doFormat(PluginTree.Node node, PluginContext context) {
        List<PluginTree.Node> nodes = new ArrayList<>();
        node.forEach(it -> {
            if (!it.isPluginNode()) {
                nodes.add(it);
            }
        });
        return doFormat(nodes, context);
    }

    public abstract T doFormat(List<PluginTree.Node> nodes, PluginContext context);
}
