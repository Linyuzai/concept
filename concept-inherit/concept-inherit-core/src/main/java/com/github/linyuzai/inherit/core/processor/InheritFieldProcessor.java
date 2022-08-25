package com.github.linyuzai.inherit.core.processor;

import com.github.linyuzai.inherit.core.annotation.InheritField;
import com.github.linyuzai.inherit.core.annotation.InheritFields;
import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;

@AutoService(Processor.class)
public class InheritFieldProcessor extends AbstractInheritProcessor {

    @Override
    protected String getAnnotationName() {
        return InheritField.class.getName();
    }

    @Override
    protected String getRepeatableAnnotationName() {
        return InheritFields.class.getName();
    }

    @Override
    protected boolean inheritFields() {
        return true;
    }
}
