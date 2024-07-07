package com.github.linyuzai.plugin.core.handle.extract;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public interface MethodPluginExtractor extends PluginExtractor {

    void addInvokerFactory(InvokerFactory factory);

    void removeInvokerFactory(InvokerFactory factory);

    void prepareInvokers();

    interface InvokerFactory {

        Invoker create(Method method, Parameter parameter);
    }
}
