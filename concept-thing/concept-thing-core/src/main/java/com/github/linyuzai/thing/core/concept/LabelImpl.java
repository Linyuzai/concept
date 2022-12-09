package com.github.linyuzai.thing.core.concept;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabelImpl extends AbstractLabel {

    private String id;

    private String key;

    private String name;

    private Category category;
}
