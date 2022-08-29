package com.github.linyuzai.concept.sample.inherit.builder;

import com.github.linyuzai.inherit.core.annotation.InheritField;
import com.github.linyuzai.inherit.core.flag.InheritFlag;

public class Inherit1 {

    private String a;

    private Boolean b;

    private boolean c;

    //@InheritField(sources = {Inherit1.class, Builder.class}, flags = InheritFlag.BUILDER)
    public static class Builder {

        private String d;
    }
}
