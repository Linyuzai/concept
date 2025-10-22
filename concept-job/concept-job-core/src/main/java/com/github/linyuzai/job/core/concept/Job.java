package com.github.linyuzai.job.core.concept;

public interface Job {

    String getId();

    String getName();

    String getDescription();

    String getParams();

    Object run(String params) throws Throwable;

    default Object run() throws Throwable {
        return run(getParams());
    }
}
