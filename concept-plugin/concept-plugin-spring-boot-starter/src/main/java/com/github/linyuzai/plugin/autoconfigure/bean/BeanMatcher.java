package com.github.linyuzai.plugin.autoconfigure.bean;

import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import com.github.linyuzai.plugin.jar.match.ClassMatcher;
import com.github.linyuzai.plugin.jar.resolve.JarClassResolver;

import java.lang.annotation.Annotation;

/**
 * 实例匹配器
 */
@HandlerDependency(JarClassResolver.class)
public class BeanMatcher extends ClassMatcher {

    public BeanMatcher(Class<?> target, Annotation[] annotations) {
        super(target, annotations);
    }
}
