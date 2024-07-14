package com.github.linyuzai.plugin.core.handle.extract.format;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于插件树节点值的格式化器
 */
public abstract class TreeValueFormatter<T> extends TreeNodeFormatter<T> {

    @Override
    public T doFormat(List<PluginTree.Node> nodes, PluginContext context) {
        return doFormat(nodes.stream().map(PluginTree.Node::getValue).collect(Collectors.toList()));
    }

    /**
     * 格式化节点值
     */
    public abstract T doFormat(List<Object> objects);
}
