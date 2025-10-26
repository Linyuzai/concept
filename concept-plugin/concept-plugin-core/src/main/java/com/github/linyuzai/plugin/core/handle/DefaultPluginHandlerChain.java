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

/**
 * 默认的插件处理链，根据插件处理器的依赖关系生成插件处理链
 */
public class DefaultPluginHandlerChain implements PluginHandlerChain {

    /**
     * 插件解析器及其对应的插件过滤器
     */
    private final List<HandlerEntry> entries = new ArrayList<>();

    /**
     * 插件提取器
     */
    private final List<PluginHandler> extractors = new ArrayList<>();

    public DefaultPluginHandlerChain(Collection<? extends PluginHandler> handlers) {
        List<PluginHandler> resolvers = new ArrayList<>();
        List<PluginHandler> filters = new ArrayList<>();
        //区分解析器，过滤器，提取器
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
        //根据依赖关系排序解析器
        List<PluginHandler> sorted = resolveDependency(resolvers);
        for (PluginHandler handler : sorted) {
            //获得每个解析器对应的过滤器
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
            entries.add(new HandlerEntry(handler, filtered));
        }
    }

    protected List<PluginHandler> resolveDependency(List<PluginHandler> unsorted) {
        List<PluginHandler> container = new ArrayList<>();
        //根据提取器筛选解析器
        //不需要提取的内容将不会被解析
        for (PluginHandler extractor : this.extractors) {
            if (extractor instanceof PluginHandler.Dependency) {
                //获得依赖的处理器类型
                Class<? extends PluginHandler>[] dependencies =
                        ((PluginHandler.Dependency) extractor).getDependencies();
                if (dependencies.length == 0) {
                    continue;
                }
                for (Class<? extends PluginHandler> dependency : dependencies) {
                    doResolveDependency(dependency, unsorted, container);
                }
            }
        }
        return container;
    }

    protected void doResolveDependency(Class<? extends PluginHandler> dependency,
                                       Collection<? extends PluginHandler> handlers,
                                       Collection<PluginHandler> container) {
        //判断是否存在处理器类型对应的处理器
        Collection<PluginHandler> find = findDependencies(dependency, handlers);
        if (find.isEmpty()) {
            throwDependencyNotFound(dependency);
        }
        //添加处理器并处理依赖关系
        for (PluginHandler dependence : find) {
            doResolveDependency(dependence, handlers, container);
        }
    }

    //TODO 循环依赖校验
    protected void doResolveDependency(PluginHandler handler,
                                       Collection<? extends PluginHandler> handlers,
                                       Collection<PluginHandler> container) {
        //Stack<PluginHandler> stack = new Stack<>();
        if (handler instanceof PluginHandler.Dependency) {
            //获得依赖的处理器类型
            Class<? extends PluginHandler>[] dependencies =
                    ((PluginHandler.Dependency) handler).getDependencies();
            if (dependencies.length == 0) {
                addHandler(handler, container);
            } else {
                for (Class<? extends PluginHandler> dependency : dependencies) {
                    if (containsDependency(dependency, container)) {
                        //依赖的处理器已经存在
                        continue;
                    }
                    doResolveDependency(dependency, handlers, container);
                }
                addHandler(handler, container);
            }
        } else {
            addHandler(handler, container);
        }
    }

    /**
     * 依赖的处理器是否已经存在
     */
    protected boolean containsDependency(Class<? extends PluginHandler> dependency,
                                         Collection<? extends PluginHandler> handlers) {
        for (PluginHandler handler : handlers) {
            if (dependency.isInstance(handler)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否存在处理器类型对应的处理器
     */
    protected Collection<PluginHandler> findDependencies(Class<? extends PluginHandler> dependency,
                                                         Collection<? extends PluginHandler> handlers) {
        return handlers.stream().filter(dependency::isInstance).collect(Collectors.toList());
    }

    /**
     * 去重添加
     */
    protected void addHandler(PluginHandler handler, Collection<PluginHandler> container) {
        if (container.contains(handler)) {
            return;
        }
        container.add(handler);
    }

    protected void throwDependencyNotFound(Class<? extends PluginHandler> dependency) {
        throw new IllegalArgumentException("Dependency not found: " + dependency);
    }

    /**
     * 执行下一个处理器
     *
     * @param context 插件上下文
     */
    @Override
    public void next(PluginContext context) {
        doNext(context, 0);
    }

    protected void doNext(PluginContext context, int index) {
        //如果解析器已经处理完成则调用提取器
        if (index >= entries.size()) {
            for (PluginHandler extractor : extractors) {
                extractor.handle(context);
            }
            return;
        }
        //获得指定的解析器
        HandlerEntry entry = entries.get(index);
        //解析
        entry.resolver.handle(context);
        //过滤
        for (PluginHandler filter : entry.filters) {
            filter.handle(context);
        }
        //触发下一个解析器或执行提取
        doNext(context, index + 1);
    }

    /**
     * 持有插件解析器和对应的插件过滤器
     */
    @Getter
    @RequiredArgsConstructor
    public static class HandlerEntry {

        private final PluginHandler resolver;

        private final List<PluginHandler> filters;
    }
}
