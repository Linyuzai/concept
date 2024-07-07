package com.github.linyuzai.plugin.jar.handle.extract;

import com.github.linyuzai.plugin.core.handle.extract.DynamicExtractor;

import java.lang.reflect.Method;

/**
 * 基于 jar 的动态插件提取器
 */
public class JarDynamicExtractor extends DynamicExtractor {

    public JarDynamicExtractor(Object target) {
        super(target);
    }

    public JarDynamicExtractor(Object target, Method... methods) {
        super(target, methods);
    }

    @Override
    public void useDefaultInvokerFactories() {
        super.useDefaultInvokerFactories();
        addInvokerFactory(new ClassExtractor.InvokerFactory());
    }
}
