package com.github.linyuzai.plugin.core.tree.trace;

import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.tree.PluginTree;

import java.util.Collection;

public interface PluginTracer {

    Collection<Object> getTraceIds();

    HandleStage getTrace(Object id);

    interface HandleStage {

        PluginTree getTree();

        PluginHandler getHandler();
    }
}
