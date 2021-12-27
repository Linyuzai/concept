package com.github.linyuzai.download.core.source.reflect;

/**
 * 反射器 / Reflector
 * 通过反射获得值 / Get value by reflection
 */
public interface Reflector {

    Object reflect(Object model) throws ReflectiveOperationException;
}
