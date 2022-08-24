package com.github.linyuzai.concept.sample.builder;

import com.github.linyuzai.builder.core.BuilderRef;

public class SampleExAndBuilder extends Sample {

    private String ex;

    public SampleExAndBuilder(String a, String b, String ex) {
        super(a, b);
        this.ex = ex;
    }

    //@BuilderRef(SampleExAndBuilder.class)
    public static class Builder {

    }
}
