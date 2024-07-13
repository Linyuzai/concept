package com.github.linyuzai.plugin.core.context;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import lombok.Getter;
import lombok.Setter;

/**
 * 插件上下文抽象类
 */
@Getter
@Setter
public abstract class AbstractPluginContext implements PluginContext {

    protected PluginContext parent;

    @Override
    public PluginConcept getConcept() {
        return get(PluginConcept.class);
    }

    @Override
    public Plugin getPlugin() {
        return get(Plugin.class);
    }

    @Override
    public PluginContext getRoot() {
        PluginContext parent = getParent();
        if (parent == null) {
            return this;
        }
        return parent.getRoot();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void destroy() {

    }
}
