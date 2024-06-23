package com.github.linyuzai.plugin.jar.handle.extract.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.extract.match.AbstractPluginMatcher;
import com.github.linyuzai.plugin.jar.handle.filter.AnnotationFilter;
import com.github.linyuzai.plugin.jar.handle.filter.ClassFilter;
import com.github.linyuzai.plugin.jar.handle.filter.ClassNameFilter;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClass;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClassResolver;

import java.lang.annotation.Annotation;

/**
 * 类匹配器
 */
@HandlerDependency(JarClassResolver.class)
public class ClassMatcher extends AbstractPluginMatcher<JarClass> {

    /**
     * 类型
     */
    protected final Class<?> target;

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
            if (annotation.annotationType() == PluginClassName.class) {
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
        return JarClass.class;
    }

    /**
     * 是对应的类或其子类并基于注解匹配
     *
     * @param clazz 类
     * @return 匹配之后的类
     */
    @Override
    public boolean doFilter(JarClass clazz, PluginContext context) {
        return applyFilters(clazz) && target.isAssignableFrom(clazz.get());
    }

    /**
     * 结合类相关的注解进行过滤
     *
     * @param clazz 类
     * @return 是否匹配
     */
    public boolean applyFilters(JarClass clazz) {
        if (classNameFilter != null && !classNameFilter.matchPattern(clazz.getName())) {
            return false;
        }
        if (classFilter != null && !classFilter.filterClass(clazz.get())) {
            return false;
        }
        if (annotationFilter != null && !annotationFilter.hasAnnotation(clazz.get())) {
            return false;
        }
        return true;
    }
}
