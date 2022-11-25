package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.common.Containable;

public interface Category extends Containable {

    String name();

    Category parent();

    Category category(String id);

    Label label(String id);

    Categories categories();

    Labels labels();
}
