package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;

public class EntryResolver implements PluginResolver {

    @Override
    public void resolve(PluginContext context) {
        PluginTree tree = context.get(PluginTree.class);
        tree.getTransformer()
                .create(this)
                .inbound(tree.getRoot())
                .transform(node -> node)
                .outboundKey(Plugin.Entry.class);
    }
}
