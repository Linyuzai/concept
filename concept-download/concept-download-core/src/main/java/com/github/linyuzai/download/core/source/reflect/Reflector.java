package com.github.linyuzai.download.core.source.reflect;

/**
 * 反射器，通过反射获得值。
 */
public interface Reflector {

    /**
     * 通过反射或者值。
     *
     * @param model 实例对象
     * @return 值
     */
    Object reflect(Object model);
}
