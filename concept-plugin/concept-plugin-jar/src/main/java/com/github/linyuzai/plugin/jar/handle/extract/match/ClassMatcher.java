package com.github.linyuzai.plugin.jar.handle.extract.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.extract.match.AbstractPluginMatcher;
import com.github.linyuzai.plugin.core.handle.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.core.handle.filter.PluginFilter;
import com.github.linyuzai.plugin.jar.handle.filter.ClassFilter;
import com.github.linyuzai.plugin.jar.handle.filter.ClassNameFilter;
import com.github.linyuzai.plugin.jar.handle.resolve.ClassSupplier;
import com.github.linyuzai.plugin.jar.handle.resolve.ClassResolver;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 类匹配器
 */
@HandlerDependency(ClassResolver.class)
public class ClassMatcher extends AbstractPluginMatcher<ClassSupplier> {

    /**
     * 类型
     */
    protected final Class<?> target;

    protected List<AbstractPluginFilter<ClassSupplier>> filters = new ArrayList<>();

    public ClassMatcher(Class<?> target, Annotation[] annotations) {
        super(annotations);
        this.target = target;
        for (Annotation annotation : annotations) {
            //先过滤名称，不会触发类加载
            if (annotation.annotationType() == PluginClassName.class) {
                String[] classNames = ((PluginClassName) annotation).value();
                if (classNames.length > 0) {
                    addFilter(new ClassNameFilter(classNames));
                }
            } else if (annotation.annotationType() == PluginClass.class) {
                Class<?>[] classes = ((PluginClass) annotation).value();
                if (classes.length > 0) {
                    addFilter(ClassFilter.create(classes));
                }
            } else if (annotation.annotationType() == PluginClassAnnotation.class) {
                Class<? extends Annotation>[] classes = ((PluginClassAnnotation) annotation).value();
                if (classes.length > 0) {
                    addFilter(ClassFilter.annotation(classes));
                }
            }
        }
    }

    public void addFilter(AbstractPluginFilter<ClassSupplier> filter) {
        this.filters.add(filter);
        sort();
    }

    public void removeFilter(AbstractPluginFilter<ClassSupplier> filter) {
        this.filters.remove(filter);
    }

    protected void sort() {
        this.filters.sort(Comparator.comparingInt(PluginFilter::getOrder));
    }

    @Override
    public Object getKey() {
        return ClassSupplier.class;
    }

    /**
     * 是对应的类或其子类并基于注解匹配
     *
     * @param classSupplier 类
     * @return 匹配之后的类
     */
    @Override
    public boolean doFilter(ClassSupplier classSupplier, PluginContext context) {
        return applyFilters(classSupplier) && target.isAssignableFrom(classSupplier.get());
    }

    /**
     * 结合类相关的注解进行过滤
     *
     * @param classSupplier 类
     * @return 是否匹配
     */
    public boolean applyFilters(ClassSupplier classSupplier) {
        for (AbstractPluginFilter<ClassSupplier> filter : filters) {
            if (!filter.applyNegation(filter.doFilter(classSupplier))) {
                return false;
            }
        }
        return true;
    }
}
