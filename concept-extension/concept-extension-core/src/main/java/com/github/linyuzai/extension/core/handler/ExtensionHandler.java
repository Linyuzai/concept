package com.github.linyuzai.extension.core.handler;

import com.github.linyuzai.extension.core.concept.Extension;

import java.util.Collection;
import java.util.Collections;

public interface ExtensionHandler {

    Collection<Extension.ArgumentAndResult> onExtend(Extension extension, Extension.Argument argument, ExtensionHandlerChain chain);

    default Collection<Extension.ArgumentAndResult> nestingExtend(Collection<? extends Extension.Argument> arguments,
                                                                  Extension extension,
                                                                  ExtensionHandlerChain chain) {
        //判断数量
        int size = arguments.size();
        if (size == 0) {
            return Collections.emptyList();
        } else if (size == 1) {
            //获得第一个并继续往下拦截
            return onExtend(extension, arguments.iterator().next(), chain);
        } else {
            //继续
            return extension.getConcept().extend(arguments);
        }
    }
}
