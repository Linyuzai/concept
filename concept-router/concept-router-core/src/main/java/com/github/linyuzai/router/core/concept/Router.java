package com.github.linyuzai.router.core.concept;

public interface Router {

    String getId();

    boolean isForced();

    boolean isEnabled();

    long getTimestamp();

    interface Source {

    }

    interface Location {

    }
}
