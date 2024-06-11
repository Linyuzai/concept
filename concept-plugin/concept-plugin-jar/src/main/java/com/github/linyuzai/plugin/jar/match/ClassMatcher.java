package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.match.AbstractPluginMatcher;
import com.github.linyuzai.plugin.jar.filter.AnnotationFilter;
import com.github.linyuzai.plugin.jar.filter.ClassFilter;
import com.github.linyuzai.plugin.jar.filter.ClassNameFilter;
import com.github.linyuzai.plugin.jar.filter.PackageFilter;
import com.github.linyuzai.plugin.jar.resolve.JarClassResolver;

import java.lang.annotation.Annotation;

/**
 * 类匹配器
 */
@HandlerDependency(JarClassResolver.class)
public class ClassMatcher extends AbstractPluginMatcher<Class<?>> {

    /**
     * 类型
     */
    protected final Class<?> target;

    /**
     * 包名过滤器
     */
    protected PackageFilter packageFilter;

    /**
     * 类名过滤器
     */
    protected ClassNameFilter classNameFilter;

    /**
     * 类过滤器
     */
    protected ClassFilter classFilter;

    /**
     * 注解过滤器
     */
    protected AnnotationFilter annotationFilter;

    public ClassMatcher(Class<?> target, Annotation[] annotations) {
        super(annotations);
        this.target = target;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == PluginPackage.class) {
                String[] packages = ((PluginPackage) annotation).value();
                if (packages.length > 0) {
                    packageFilter = new PackageFilter(packages);
                }
            } else if (annotation.annotationType() == PluginClassName.class) {
                String[] classNames = ((PluginClassName) annotation).value();
                if (classNames.length > 0) {
                    classNameFilter = new ClassNameFilter(classNames);
                }
            } else if (annotation.annotationType() == PluginClass.class) {
                Class<?>[] classes = ((PluginClass) annotation).value();
                if (classes.length > 0) {
                    classFilter = new ClassFilter(classes);
                }
            } else if (annotation.annotationType() == PluginAnnotation.class) {
                Class<? extends Annotation>[] classes = ((PluginAnnotation) annotation).value();
                if (classes.length > 0) {
                    annotationFilter = new AnnotationFilter(classes);
                }
            }
        }
    }

    @Override
    public Object getKey() {
        return Class.class;
    }

    /**
     * 是对应的类或其子类并基于注解匹配
     *
     * @param clazz 类
     * @return 匹配之后的类
     */
    @Override
    public boolean doFilter(Class<?> clazz, PluginContext context) {
        return target.isAssignableFrom(clazz) && applyFilters(clazz);
    }

    /**
     * 结合类相关的注解进行过滤
     *
     * @param clazz 类
     * @return 是否匹配
     */
    public boolean applyFilters(Class<?> clazz) {
        if (packageFilter != null && !packageFilter.matchPackage(clazz.getName())) {
            return false;
        }
        if (classNameFilter != null && !classNameFilter.matchClassName(clazz.getName())) {
            return false;
        }
        if (classFilter != null && !classFilter.matchClass(clazz)) {
            return false;
        }
        if (annotationFilter != null && !annotationFilter.hasAnnotation(clazz)) {
            return false;
        }
        return true;
    }
}
