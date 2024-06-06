package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.concept.PluginEntry;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import lombok.SneakyThrows;

public class EntryResolver implements PluginResolver {

    @SneakyThrows
    @Override
    public void resolve(PluginContext context) {
        PluginTree tree = context.get(PluginTree.class);
        tree.getTransformer()
                .create(this)
                .inbound(tree.getRoot())
                .transform(node -> node)
                .outboundKey(PluginEntry.class);

    }

    @Override
    public boolean support(PluginContext context) {
        return true;
    }
}
