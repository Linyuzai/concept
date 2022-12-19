package com.github.linyuzai.inherit.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;

/**
 * com.github.linyuzai.inherit.core.annotation.InheritClass 处理器
 */
@AutoService(Processor.class)
public class InheritClassProcessor extends AbstractInheritProcessor {

    @Override
    protected String getAnnotationName() {
        return "com.github.linyuzai.inherit.core.annotation.InheritClass";
    }

    @Override
    protected String getRepeatableAnnotationName() {
        return "com.github.linyuzai.inherit.core.annotation.InheritClasses";
    }

    @Override
    protected boolean inheritFields() {
        return true;
    }

    @Override
    protected boolean inheritMethods() {
        return true;
    }
}
