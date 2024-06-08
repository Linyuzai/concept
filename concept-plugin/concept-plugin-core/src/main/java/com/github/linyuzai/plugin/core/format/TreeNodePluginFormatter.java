package com.github.linyuzai.plugin.core.format;

import com.github.linyuzai.plugin.core.tree.PluginTree;

import java.util.ArrayList;
import java.util.List;

public abstract class TreeNodePluginFormatter<T> extends AbstractPluginFormatter<PluginTree.Node, T> {

    @Override
    public T doFormat(PluginTree.Node node) {
        List<PluginTree.Node> nodes = new ArrayList<>();
        node.forEach(it -> {
            if (!it.isPluginNode()) {
                nodes.add(it);
            }
        });
        return formatNodes(nodes);
    }

    public abstract T formatNodes(List<PluginTree.Node> nodes);
}
