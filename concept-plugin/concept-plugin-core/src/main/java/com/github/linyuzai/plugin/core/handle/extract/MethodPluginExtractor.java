package com.github.linyuzai.plugin.core.handle.extract;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 基于方法的插件提取器
 */
public interface MethodPluginExtractor extends PluginExtractor {

    /**
     * 添加提取执行器工厂
     */
    void addInvokerFactory(InvokerFactory factory);

    /**
     * 移除提取执行器工厂
     */
    void removeInvokerFactory(InvokerFactory factory);

    /**
     * 准备提取执行器
     */
    void initialize();

    /**
     * 提取执行器工厂
     */
    interface InvokerFactory {

        /**
         * 创建提取执行器
         */
        Invoker create(Method method, Parameter parameter);
    }
}
