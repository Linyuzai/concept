package com.github.linyuzai.plugin.core.handle;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.extract.PluginExtractor;
import com.github.linyuzai.plugin.core.handle.filter.PluginFilter;
import com.github.linyuzai.plugin.core.handle.resolve.PluginResolver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultPluginHandlerChain implements PluginHandlerChain {

    private final List<Entry> entries = new ArrayList<>();

    private final List<PluginHandler> extractors = new ArrayList<>();

    public DefaultPluginHandlerChain(Collection<? extends PluginHandler> handlers) {
        List<PluginHandler> resolvers = new ArrayList<>();
        List<PluginHandler> filters = new ArrayList<>();
        for (PluginHandler handler : handlers) {
            if (handler instanceof PluginResolver) {
                resolvers.add(handler);
            }
            if (handler instanceof PluginFilter) {
                filters.add(handler);
            }
            if (handler instanceof PluginExtractor) {
                extractors.add(handler);
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
                    }).map(PluginFilter.class::cast)
                    .sorted(Comparator.comparingInt(f -> f == null ? 0 : f.getOrder()))
                    .collect(Collectors.toList());
            entries.add(new Entry(handler, filtered));
        }
    }

    protected List<PluginHandler> resolveDependency(List<PluginHandler> unsorted) {
        List<PluginHandler> container = new ArrayList<>();
        //根据提取器筛选解析器
        for (PluginHandler extractor : this.extractors) {
            if (extractor instanceof PluginHandler.Dependency) {
                Class<? extends PluginHandler>[] dependencies =
                        ((PluginHandler.Dependency) extractor).getDependencies();
                if (dependencies.length == 0) {
                    continue;
                }
                for (Class<? extends PluginHandler> dependency : dependencies) {
                    PluginHandler dependence = findDependency(dependency, unsorted);
                    if (dependence == null) {
                        throwDependencyNotFound(dependency);
                    }
                    resolveDependency(dependence, unsorted, container);
                }
            }
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
                addHandler(handler, container);
            } else {
                for (Class<? extends PluginHandler> dependency : dependencies) {
                    if (containsDependency(dependency, container)) {
                        continue;
                    }
                    PluginHandler dependence = findDependency(dependency, handlers);
                    if (dependence == null) {
                        throwDependencyNotFound(dependency);
                    }
                    resolveDependency(dependence, handlers, container);
                }
                addHandler(handler, container);
            }
        } else {
            addHandler(handler, container);
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

    protected void addHandler(PluginHandler handler, Collection<PluginHandler> container) {
        if (container.contains(handler)) {
            return;
        }
        container.add(handler);
    }

    protected void throwDependencyNotFound(Class<? extends PluginHandler> dependency) {
        throw new IllegalArgumentException("Dependency not found: " + dependency);
    }

    @Override
    public void next(PluginContext context) {
        doNext(context, 0);
    }

    protected void doNext(PluginContext context, int index) {
        if (index >= entries.size()) {
            for (PluginHandler extractor : extractors) {
                extractor.handle(context);
            }
            return;
        }
        Entry entry = entries.get(index);
        entry.resolver.handle(context);
        for (PluginHandler filter : entry.filters) {
            filter.handle(context);
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
