package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.container.Categories;
import com.github.linyuzai.thing.core.container.Labels;
import com.github.linyuzai.thing.core.context.ThingContext;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryImpl extends AbstractCategory {

    private String id;

    private String key;

    private String name;

    private Category parent;

    private Categories categories;

    private Labels labels;

    private ThingContext context;
}
