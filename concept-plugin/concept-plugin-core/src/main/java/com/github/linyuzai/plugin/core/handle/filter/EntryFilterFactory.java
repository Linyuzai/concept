package com.github.linyuzai.plugin.core.handle.filter;

import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.PropertyPluginHandlerFactory;

public class EntryFilterFactory extends PropertyPluginHandlerFactory {

    @Override
    public String getPropertyName() {
        return PluginFilter.PropertyKey.PREFIX + "entry";
    }

    @Override
    public PluginHandler doCreate(String[] patterns) {
        return new EntryFilter(patterns);
    }
}
