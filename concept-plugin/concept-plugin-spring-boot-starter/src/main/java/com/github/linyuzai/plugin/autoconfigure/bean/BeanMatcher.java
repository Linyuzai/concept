package com.github.linyuzai.plugin.autoconfigure.bean;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.core.handle.extract.match.AbstractPluginMatcher;
import com.github.linyuzai.plugin.jar.handle.extract.match.ClassMatcher;
import com.github.linyuzai.plugin.jar.handle.filter.ClassFilter;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

/**
 * 实例匹配器
 */
@Getter
@HandlerDependency(BeanResolver.class)
public class BeanMatcher extends AbstractPluginMatcher<BeanSupplier> {

    private final ClassMatcher classMatcher;

    public BeanMatcher(Class<?> target, Annotation[] annotations) {
        super(annotations);
        this.classMatcher = new ClassMatcher(target, annotations);
        this.classMatcher.addFilter(ClassFilter.modifier(Modifier::isInterface, Modifier::isAbstract).negate());
        this.classMatcher.addFilter(ClassFilter.isEnum().negate());
    }

    @Override
    public Object getKey() {
        return BeanSupplier.class;
    }

    @Override
    public boolean doFilter(BeanSupplier beanSupplier, PluginContext context) {
        return classMatcher.doFilter(beanSupplier.getClassSupplier(), context);
    }
}
