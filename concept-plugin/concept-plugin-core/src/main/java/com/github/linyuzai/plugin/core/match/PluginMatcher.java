package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.PluginResolverDependency;

public interface PluginMatcher extends PluginResolverDependency {

    Object match(PluginContext context);

    interface MapConvertor {

        Class<?> getMapClass();
    }

    interface ListConvertor {

        Class<?> getListClass();
    }

    interface SetConvertor {

        Class<?> getSetClass();
    }

    interface ArrayConvertor {

        Class<?> getArrayClass();
    }

    interface ObjectConvertor {

        String getType();
    }
}
