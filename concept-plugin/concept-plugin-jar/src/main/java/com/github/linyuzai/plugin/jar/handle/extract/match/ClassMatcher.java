package com.github.linyuzai.plugin.jar.handle.extract.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.extract.match.AbstractPluginMatcher;
import com.github.linyuzai.plugin.core.handle.filter.AbstractPluginFilter;
import com.github.linyuzai.plugin.jar.handle.filter.ClassAnnotationFilter;
import com.github.linyuzai.plugin.jar.handle.filter.ClassFilter;
import com.github.linyuzai.plugin.jar.handle.filter.ClassNameFilter;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClass;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClassResolver;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * 类匹配器
 */
@HandlerDependency(JarClassResolver.class)
public class ClassMatcher extends AbstractPluginMatcher<JarClass> {

    /**
     * 类型
     */
    protected final Class<?> target;

    protected List<AbstractPluginFilter<JarClass>> filters = new ArrayList<>();

    public ClassMatcher(Class<?> target, Annotation[] annotations) {
        super(annotations);
        this.target = target;
        for (Annotation annotation : annotations) {
            //先过滤名称，不会触发类加载
            if (annotation.annotationType() == PluginClassName.class) {
                String[] classNames = ((PluginClassName) annotation).value();
                if (classNames.length > 0) {
                    filters.add(new ClassNameFilter(classNames));
                }
            } else if (annotation.annotationType() == PluginClass.class) {
                Class<?>[] classes = ((PluginClass) annotation).value();
                if (classes.length > 0) {
                    filters.add(new ClassFilter(classes));
                }
            } else if (annotation.annotationType() == PluginClassAnnotation.class) {
                Class<? extends Annotation>[] classes = ((PluginClassAnnotation) annotation).value();
                if (classes.length > 0) {
                    filters.add(new ClassAnnotationFilter(classes));
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
     * @param jarClass 类
     * @return 匹配之后的类
     */
    @Override
    public boolean doFilter(JarClass jarClass, PluginContext context) {
        return applyFilters(jarClass) && target.isAssignableFrom(jarClass.get());
    }

    /**
     * 结合类相关的注解进行过滤
     *
     * @param jarClass 类
     * @return 是否匹配
     */
    public boolean applyFilters(JarClass jarClass) {
        for (AbstractPluginFilter<JarClass> filter : filters) {
            if (!filter.doFilter(jarClass)) {
                return false;
            }
        }
        return true;
    }
}
