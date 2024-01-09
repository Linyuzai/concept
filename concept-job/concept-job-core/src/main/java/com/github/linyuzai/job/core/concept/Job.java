package com.github.linyuzai.job.core.concept;

import java.util.Map;

public interface Job {

    String getId();

    String getName();

    String getDescription();

    Map<String, String> getParams();

    Object run(Map<String, String> params) throws Throwable;

    default Object run() throws Throwable {
        return run(getParams());
    }
}
