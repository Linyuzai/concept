package com.github.linyuzai.extension.core.concept.simple;

import com.github.linyuzai.extension.core.concept.Extension;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleArgumentAndResult implements Extension.ArgumentAndResult {

    private final Extension.Argument argument;

    private final Extension.Result result;
}
