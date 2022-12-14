package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.concept.Label;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractLabels extends AbstractContainer<Label> implements Labels {

    private Category category;
}
