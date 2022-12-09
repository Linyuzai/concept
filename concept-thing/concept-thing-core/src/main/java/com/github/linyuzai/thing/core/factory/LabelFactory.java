package com.github.linyuzai.thing.core.factory;

import com.github.linyuzai.thing.core.concept.Label;
import com.github.linyuzai.thing.core.container.Labels;

public interface LabelFactory {

    Label create();

    Labels createContainer();
}
