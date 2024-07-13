package com.github.linyuzai.plugin.core.intercept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginLoadException;
import com.github.linyuzai.plugin.core.repository.LinkedPluginRepository;
import com.github.linyuzai.plugin.core.repository.PluginRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Deprecated
public class DependencyInterceptor implements PluginInterceptor {

    @Override
    public void beforeLoaded(Plugin plugin, PluginContext context) {
        if (plugin.getSource() instanceof Plugin.Entry) {
            return;
        }
        String name = plugin.getMetadata().asStandard().getName();
        if (name == null || name.isEmpty()) {
            return;
        }
        PluginRepository repository = getRepository(context);
        List<Plugin> plugins = plugin.getConcept()
                .getRepository()
                .stream()
                .collect(Collectors.toList());
        PluginContext subContext = context.createSubContext();
        for (Plugin p : plugins) {
            Set<String> names = p.getMetadata().asStandard().getDependency().getNames();
            if (names == null) {
                continue;
            }
            for (String n : names) {
                if (name.equals(n)) {

                }
            }
        }
    }

    protected PluginRepository getRepository(PluginContext context) {
        PluginRepository repository = context.getRoot().get(PluginRepository.class);
        if (repository == null) {
            PluginRepository create = new LinkedPluginRepository();
            context.getRoot().set(PluginRepository.class, create);
            return create;
        } else {
            return repository;
        }
    }

    @Override
    public void afterLoaded(Plugin plugin, PluginContext context) {
        PluginRepository repository = context.getRoot().get(PluginRepository.class);
        if (repository == null) {
            return;
        }
        List<Plugin> plugins = repository.stream().collect(Collectors.toList());
        for (Plugin p : plugins) {

        }
    }

    @Override
    public void beforeUnloaded(Plugin plugin, PluginContext context) {

    }

    @Override
    public void afterUnloaded(Plugin plugin, PluginContext context) {

    }

}
