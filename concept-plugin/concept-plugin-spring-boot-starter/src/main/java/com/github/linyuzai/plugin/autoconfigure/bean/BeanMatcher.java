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
 * Bean 匹配器
 */
@Getter
@HandlerDependency(BeanResolver.class)
public class BeanMatcher extends AbstractPluginMatcher<BeanSupplier> {

    private final ClassMatcher classMatcher;

    public BeanMatcher(Class<?> target, Annotation[] annotations) {
        super(annotations);
        this.classMatcher = new ClassMatcher(target, annotations);
        //接口和抽象类不能实例化
        this.classMatcher.addFilter(ClassFilter.modifier(Modifier::isInterface, Modifier::isAbstract).negate());
        //忽略枚举类
        this.classMatcher.addFilter(ClassFilter.isEnum().negate());
        //忽略匿名类
        this.classMatcher.addFilter(ClassFilter.isAnonymous().negate());
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
