package com.github.linyuzai.plugin.core.conflict;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.exception.PluginConflictException;

public interface PluginConflictStrategy {

    Plugin getPlugin(Plugin plugin, Plugin newPlugin);

    class Keep implements PluginConflictStrategy {

        @Override
        public Plugin getPlugin(Plugin plugin, Plugin newPlugin) {
            return plugin;
        }
    }

    class Cover implements PluginConflictStrategy {

        @Override
        public Plugin getPlugin(Plugin plugin, Plugin newPlugin) {
            return newPlugin;
        }
    }

    class Error implements PluginConflictStrategy {

        @Override
        public Plugin getPlugin(Plugin plugin, Plugin newPlugin) {
            throw new PluginConflictException(plugin, newPlugin);
        }
    }
}
