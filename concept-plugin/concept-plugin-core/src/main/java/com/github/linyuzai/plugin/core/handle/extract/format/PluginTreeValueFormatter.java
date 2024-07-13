package com.github.linyuzai.plugin.core.handle.extract.format;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于
 */
public abstract class PluginTreeValueFormatter<T> extends PluginTreeNodeFormatter<T> {

    @Override
    public T doFormat(List<PluginTree.Node> nodes, PluginContext context) {
        return doFormat(nodes.stream().map(PluginTree.Node::getValue).collect(Collectors.toList()));
    }

    public abstract T doFormat(List<Object> objects);
}
