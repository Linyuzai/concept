package com.github.linyuzai.concept.sample.builder;

import com.github.linyuzai.builder.core.BuilderRef;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SampleAndBuilderEx {

    private String a;

    private String b;

    private String ex;

    //@BuilderRef(SampleAndBuilderEx.class)
    public static class BuilderEx extends Sample.Builder {


    }
}
