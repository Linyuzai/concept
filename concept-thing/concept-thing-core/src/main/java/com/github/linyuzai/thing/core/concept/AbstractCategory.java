package com.github.linyuzai.thing.core.concept;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractCategory implements Category {

    private String id;

    private String name;

    private Category category;

    private Categories categories;

    private Labels labels;
}
