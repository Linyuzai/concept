package com.github.linyuzai.inherit.core.processor;

import com.github.linyuzai.inherit.core.annotation.InheritClass;
import com.github.linyuzai.inherit.core.annotation.InheritClasses;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;

@AutoService(Processor.class)
public class InheritClassProcessor extends AbstractInheritProcessor {

    @Override
    protected String getAnnotationName() {
        return InheritClass.class.getName();
    }

    @Override
    protected String getRepeatableAnnotationName() {
        return InheritClasses.class.getName();
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
