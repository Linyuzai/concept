package com.github.linyuzai.attribute.dynamic.core.concept;

public interface Attribute {

    String getId();

    String getName();

    <T> T getValue();


}
