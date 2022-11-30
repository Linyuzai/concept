package com.github.linyuzai.thing.core.container;

import com.github.linyuzai.thing.core.concept.Category;
import com.github.linyuzai.thing.core.concept.Label;
import com.github.linyuzai.thing.core.context.ThingContext;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractLabels extends AbstractContainer<Label> implements Labels, Labels.Modifiable {

    private Category category;

    public ThingContext getContext() {
        return category.getContext();
    }

    @Override
    protected void onPrepare(Label add) {

    }

    @Override
    protected void onValid(Label add) {

    }
}
