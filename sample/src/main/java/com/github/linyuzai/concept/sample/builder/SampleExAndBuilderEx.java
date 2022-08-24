package com.github.linyuzai.concept.sample.builder;

import com.github.linyuzai.builder.core.BuilderRef;

public class SampleExAndBuilderEx extends Sample {

    private String ex;

    public SampleExAndBuilderEx(String a, String b, String ex) {
        super(a, b);
        this.ex = ex;
    }

    //@BuilderRef(SampleExAndBuilderEx.class)
    public static class BuilderEx extends Builder {

    }
}
