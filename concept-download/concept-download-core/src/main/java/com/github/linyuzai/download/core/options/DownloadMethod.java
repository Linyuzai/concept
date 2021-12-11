package com.github.linyuzai.download.core.options;

import lombok.Builder;
import lombok.Value;

import java.lang.reflect.Method;

@Value
@Builder(builderClassName = "Builder")
public class DownloadMethod {

    Method method;

    Object[] parameters;

    Object returnValue;
}
