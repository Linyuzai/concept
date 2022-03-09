package com.github.linyuzai.plugin.core.resolver;

import com.github.linyuzai.plugin.core.context.PluginContext;

public interface PluginResolver {

    boolean support(PluginContext context);

    void resolve(PluginContext context, PluginResolverChain chain);

    default Class<? extends PluginResolver>[] dependencies() {
        //getClass()
        return new Class[]{};
    }

    interface Callback {

        void onResolve(Object resolved);
    }
}
