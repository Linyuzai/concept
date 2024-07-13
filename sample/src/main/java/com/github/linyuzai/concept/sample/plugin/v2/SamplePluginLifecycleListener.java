package com.github.linyuzai.concept.sample.plugin.v2;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginLifecycleListener;
import com.github.linyuzai.plugin.core.context.PluginContext;
import org.springframework.stereotype.Component;

@Component
public class SamplePluginLifecycleListener implements PluginLifecycleListener {

    @Override
    public void onCreate(Plugin plugin) {

    }

    @Override
    public void onPrepare(Plugin plugin) {

    }

    @Override
    public void onLoaded(Plugin plugin) {

    }

    @Override
    public void onUnloaded(Plugin plugin) {

    }
}
