package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.match.AbstractPluginMatcher;
import com.github.linyuzai.plugin.jar.filter.AnnotationFilter;
import com.github.linyuzai.plugin.jar.filter.ClassFilter;
import com.github.linyuzai.plugin.jar.filter.ClassNameFilter;
import com.github.linyuzai.plugin.jar.filter.PackageFilter;
import lombok.Getter;

import java.lang.annotation.Annotation;

/**
 * 针对 jar 的插件匹配器
 *
 * @param <T> 插件类型
 */
@Getter
public abstract class AbstractJarPluginMatcher<T> extends AbstractPluginMatcher<T> {

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

    public AbstractJarPluginMatcher(Class<?> target, Annotation[] annotations) {
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

    /**
     * 结合类相关的注解进行过滤
     *
     * @param pathAndName 路径和名称
     * @param clazz       类
     * @return 是否匹配
     */
    public boolean filterWithAnnotation(String pathAndName, Class<?> clazz) {
        if (!filterWithAnnotation(pathAndName)) {
            return false;
        }
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
