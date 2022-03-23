package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.filter.NameFilter;
import com.github.linyuzai.plugin.core.filter.PathFilter;
import lombok.Getter;
import lombok.NonNull;

import java.lang.annotation.Annotation;

@Getter
public abstract class AbstractPluginMatcher<T, R> implements PluginMatcher {

    private final PluginConvertor convertor;

    private PathFilter pathFilter;

    private NameFilter nameFilter;

    public AbstractPluginMatcher(@NonNull Annotation[] annotations, @NonNull PluginConvertor convertor) {
        this.convertor = convertor;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == PluginPath.class) {
                String[] packages = ((PluginPath) annotation).value();
                if (packages.length > 0) {
                    pathFilter = new PathFilter(packages);
                }
            } else if (annotation.annotationType() == PluginName.class) {
                String[] classNames = ((PluginName) annotation).value();
                if (classNames.length > 0) {
                    nameFilter = new NameFilter(classNames);
                }
            }
        }
    }

    @Override
    public Object match(PluginContext context) {
        T source = context.get(getKey());
        R filter = filter(source);
        if (isEmpty(filter)) {
            return null;
        }
        return convertor.convert(filter);
    }

    public boolean filterWithAnnotation(String pathAndName) {
        if (pathFilter != null && !pathFilter.matchPath(pathAndName)) {
            return false;
        }
        if (nameFilter != null && !nameFilter.matchName(pathAndName)) {
            return false;
        }
        return true;
    }

    public abstract Object getKey();

    public abstract R filter(T source);

    public abstract boolean isEmpty(R filter);
}
