package com.github.linyuzai.concept.sample.builder;

import com.github.linyuzai.builder.core.BuilderRef;

public class Sample {

    private String a;

    @BuilderRef(Sample.class)
    public static class Builder {

    }
}
