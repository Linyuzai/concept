package com.github.linyuzai.plugin.core.tree.transform;

import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.tree.PluginTree;

import java.util.function.Function;

public interface PluginTransformer {

    InboundStage create(PluginHandler handler);

    interface InboundStage {

        TransformStage inbound(PluginTree.Node node);

        TransformStage inboundKey(Object inboundKey);
    }

    interface TransformStage {

        OutboundStage transform(Function<PluginTree.Node, PluginTree.Node> transform);
    }

    interface OutboundStage {

        PluginTree.Node outbound();

        void outboundKey(Object outboundKey);
    }
}
