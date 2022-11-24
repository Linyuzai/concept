package com.github.linyuzai.extension.core.factory;

import com.github.linyuzai.extension.core.concept.Extension;
import com.github.linyuzai.extension.core.concept.simple.SimpleResult;

public class DefaultExceptionResultFactory implements ExceptionResultFactory {

    @Override
    public Extension.Result create(Throwable e, Extension.Argument argument, Extension extension) {
        return new SimpleResult.Builder()
                .success(false)
                .code(null)
                .message(e.getMessage())
                .object(e)
                .build();
    }
}
