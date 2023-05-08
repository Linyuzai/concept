package com.github.linyuzai.cloud.web.core.concept;

public interface Request {

    String getMethod();

    String getPath();

    String getHeader(String name);
}
