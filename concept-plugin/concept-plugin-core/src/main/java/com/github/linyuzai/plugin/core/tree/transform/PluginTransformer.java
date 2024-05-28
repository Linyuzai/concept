package com.github.linyuzai.plugin.core.tree.transform;

import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.tree.PluginTree;

import java.util.function.Function;

public interface PluginTransformer {

    ParameterStage create(PluginHandler handler);

    interface ParameterStage {

        TransformStage parameter(PluginTree.Node node);

        TransformStage parameterId(Object parameterId);
    }

    interface TransformStage {

        ResultStage transform(Function<PluginTree.Node, PluginTree.Node> transform);
    }

    interface ResultStage {

        PluginTree.Node result();

        void resultId(Object resultId);
    }
}
