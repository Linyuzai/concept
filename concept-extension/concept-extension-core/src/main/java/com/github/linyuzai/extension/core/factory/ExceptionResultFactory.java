package com.github.linyuzai.extension.core.factory;

import com.github.linyuzai.extension.core.concept.Extension;

public interface ExceptionResultFactory {

    Extension.Result create(Throwable e, Extension.Argument argument, Extension extension);
}
