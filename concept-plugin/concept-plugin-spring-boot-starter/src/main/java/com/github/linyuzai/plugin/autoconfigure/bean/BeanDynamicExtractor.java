package com.github.linyuzai.plugin.autoconfigure.bean;

import com.github.linyuzai.plugin.jar.handle.extract.JarDynamicExtractor;
import lombok.Getter;

import java.lang.reflect.Method;

/**
 * 基于 Spring 的动态插件提取器
 */
@Getter
public class BeanDynamicExtractor extends JarDynamicExtractor {

    public BeanDynamicExtractor(Object target) {
        super(target);
    }

    public BeanDynamicExtractor(Object target, Method... methods) {
        super(target, methods);
    }

    @Override
    public void useDefaultInvokerFactories() {
        super.useDefaultInvokerFactories();
        addInvokerFactory(new BeanExtractor.InvokerFactory());
    }
}
