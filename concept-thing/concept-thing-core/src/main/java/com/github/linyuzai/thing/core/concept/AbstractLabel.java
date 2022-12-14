package com.github.linyuzai.thing.core.concept;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractLabel extends AbstractIdentify<Label> implements Label {

    private String name;

    private Category category;
}
