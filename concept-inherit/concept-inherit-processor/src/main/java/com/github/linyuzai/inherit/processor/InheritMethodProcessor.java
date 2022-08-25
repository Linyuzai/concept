package com.github.linyuzai.inherit.processor;

import com.github.linyuzai.inherit.core.annotation.InheritMethod;
import com.github.linyuzai.inherit.core.annotation.InheritMethods;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;

@AutoService(Processor.class)
public class InheritMethodProcessor extends AbstractInheritProcessor {

    @Override
    protected String getAnnotationName() {
        return InheritMethod.class.getName();
    }

    @Override
    protected String getRepeatableAnnotationName() {
        return InheritMethods.class.getName();
    }

    @Override
    protected boolean inheritMethods() {
        return true;
    }
}
