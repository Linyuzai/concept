package com.github.linyuzai.concept.sample.inherit.builder;

import com.github.linyuzai.inherit.core.annotation.InheritClass;
import com.github.linyuzai.inherit.core.flag.InheritFlags;

public class Inherit2 extends Inherit1 {

    @InheritClass(sources = Inherit2.class, flags = InheritFlags.BUILDER)
    public static class Builder extends Inherit1.Builder {

    }
}
