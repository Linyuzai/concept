package com.github.linyuzai.plugin.core.conflict;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.exception.PluginConflictException;

public interface PluginConflictStrategy {

    Plugin getPlugin(String id, Plugin curPlugin, Plugin newPlugin);

    class Keep implements PluginConflictStrategy {

        @Override
        public Plugin getPlugin(String id, Plugin curPlugin, Plugin newPlugin) {
            return curPlugin;
        }
    }

    class Cover implements PluginConflictStrategy {

        @Override
        public Plugin getPlugin(String id, Plugin curPlugin, Plugin newPlugin) {
            return newPlugin;
        }
    }

    class Error implements PluginConflictStrategy {

        @Override
        public Plugin getPlugin(String id, Plugin curPlugin, Plugin newPlugin) {
            throw new PluginConflictException(id, curPlugin, newPlugin);
        }
    }
}
