package com.github.linyuzai.concept.sample.builder;

import com.github.linyuzai.builder.core.BuilderRef;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Sample {

    private String a;

    public static final String X = "1";

    private String b;

    @BuilderRef(Sample.class)
    public static class Builder {

    }
}
