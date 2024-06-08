package com.github.linyuzai.plugin.core.handle;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.filter.PluginFilter;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultPluginHandlerChain implements PluginHandlerChain {

    private final List<Entry> entries = new ArrayList<>();

    public DefaultPluginHandlerChain(List<PluginHandler> handlers) {
        List<PluginHandler> resolvers = new ArrayList<>();
        List<PluginHandler> filters = new ArrayList<>();
        for (PluginHandler handler : handlers) {
            if (handler instanceof PluginResolver) {
                resolvers.add(handler);
            }
            if (handler instanceof PluginFilter) {
                filters.add(handler);
            }
        }
        List<PluginHandler> sorted = resolveDependency(resolvers);
        for (PluginHandler handler : sorted) {
            List<PluginHandler> filtered = filters.stream().filter(it -> {
                if (it instanceof PluginHandler.Dependency) {
                    Class<? extends PluginHandler>[] dependencies =
                            ((PluginHandler.Dependency) it).getDependencies();
                    for (Class<? extends PluginHandler> dependency : dependencies) {
                        if (dependency.isInstance(handler)) {
                            return true;
                        }
                    }
                    return false;
                } else {
                    return true;
                }
            }).collect(Collectors.toList());
            entries.add(new Entry(handler, filtered));
        }
    }

    protected List<PluginHandler> resolveDependency(List<PluginHandler> unsorted) {
        List<PluginHandler> container = new ArrayList<>();
        for (PluginHandler handler : unsorted) {
            resolveDependency(handler, unsorted, container);
        }
        return container;
    }

    //TODO 循环依赖校验
    protected void resolveDependency(PluginHandler handler,
                                     Collection<PluginHandler> handlers,
                                     Collection<PluginHandler> container) {
        //Stack<PluginHandler> stack = new Stack<>();
        if (handler instanceof PluginHandler.Dependency) {
            Class<? extends PluginHandler>[] dependencies =
                    ((PluginHandler.Dependency) handler).getDependencies();
            if (dependencies.length == 0) {
                container.add(handler);
            } else {
                for (Class<? extends PluginHandler> dependency : dependencies) {
                    if (containsDependency(dependency, container)) {
                        continue;
                    }
                    PluginHandler dependence = findDependency(dependency, handlers);
                    if (dependence == null) {
                        throw new IllegalArgumentException("Dependency not found: " + dependency);
                    }
                    resolveDependency(dependence, handlers, container);
                }
                container.add(handler);
            }
        } else {
            container.add(handler);
        }
    }

    protected boolean containsDependency(Class<? extends PluginHandler> dependency,
                                         Collection<? extends PluginHandler> handlers) {
        for (PluginHandler handler : handlers) {
            if (dependency.isInstance(handler)) {
                return true;
            }
        }
        return false;
    }

    protected PluginHandler findDependency(Class<? extends PluginHandler> dependency,
                                           Collection<? extends PluginHandler> handlers) {
        for (PluginHandler handler : handlers) {
            if (dependency.isInstance(handler)) {
                return handler;
            }
        }
        return null;
    }

    @Override
    public void next(PluginContext context) {
        doNext(context, 0);
    }

    protected void doNext(PluginContext context, int index) {
        if (index >= entries.size()) {
            return;
        }
        Entry entry = entries.get(index);
        ((PluginResolver) entry.resolver).resolve(context);
        for (PluginHandler filter : entry.filters) {
            ((PluginFilter) filter).filter(context);
        }
        doNext(context, index + 1);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Entry {

        private final PluginHandler resolver;

        private final List<PluginHandler> filters;
    }
}
