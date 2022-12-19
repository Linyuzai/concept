package com.github.linyuzai.inherit.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;

/**
 * com.github.linyuzai.inherit.core.annotation.InheritField 处理器
 */
@AutoService(Processor.class)
public class InheritFieldProcessor extends AbstractInheritProcessor {

    @Override
    protected String getAnnotationName() {
        return "com.github.linyuzai.inherit.core.annotation.InheritField";
    }

    @Override
    protected String getRepeatableAnnotationName() {
        return "com.github.linyuzai.inherit.core.annotation.InheritFields";
    }

    @Override
    protected boolean inheritFields() {
        return true;
    }
}
