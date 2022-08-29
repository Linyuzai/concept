package com.github.linyuzai.concept.sample.inherit.builder;

import com.github.linyuzai.inherit.core.annotation.InheritClass;
import com.github.linyuzai.inherit.core.flag.InheritFlag;

public class Inherit2 extends Inherit1 {

    @InheritClass(sources = Inherit2.class, flags = InheritFlag.BUILDER)
    public static class Builder extends Inherit1.Builder {

    }
}
