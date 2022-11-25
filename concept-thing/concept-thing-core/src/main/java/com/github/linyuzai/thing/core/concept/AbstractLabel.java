package com.github.linyuzai.thing.core.concept;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractLabel implements Label {

    private String id;

    private String name;

    private Category category;
}
