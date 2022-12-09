package com.github.linyuzai.thing.core.factory;

import com.github.linyuzai.thing.core.concept.Label;
import com.github.linyuzai.thing.core.concept.LabelImpl;
import com.github.linyuzai.thing.core.container.Labels;
import com.github.linyuzai.thing.core.container.LabelsImpl;

public class LabelFactoryImpl implements LabelFactory{

    @Override
    public Label create() {
        return new LabelImpl();
    }

    @Override
    public Labels createContainer() {
        return new LabelsImpl();
    }
}
