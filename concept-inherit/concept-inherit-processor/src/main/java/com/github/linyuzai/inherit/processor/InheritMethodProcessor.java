package com.github.linyuzai.inherit.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;

/**
 * com.github.linyuzai.inherit.core.annotation.InheritMethod 处理器
 */
@AutoService(Processor.class)
public class InheritMethodProcessor extends AbstractInheritProcessor {

    @Override
    protected String getAnnotationName() {
        return "com.github.linyuzai.inherit.core.annotation.InheritMethod";
    }

    @Override
    protected String getRepeatableAnnotationName() {
        return "com.github.linyuzai.inherit.core.annotation.InheritMethods";
    }

    @Override
    protected boolean inheritMethods() {
        return true;
    }
}
