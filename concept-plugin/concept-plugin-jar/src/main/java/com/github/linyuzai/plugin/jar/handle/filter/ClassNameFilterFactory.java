package com.github.linyuzai.plugin.jar.handle.filter;

import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.PropertyPluginHandlerFactory;
import com.github.linyuzai.plugin.core.handle.filter.PluginFilter;

public class ClassNameFilterFactory extends PropertyPluginHandlerFactory {

    @Override
    public String getPropertyName() {
        return PluginFilter.PropertyKey.PREFIX + "class";
    }

    @Override
    public PluginHandler doCreate(String[] patterns) {
        return new ClassNameFilter(patterns);
    }
}
