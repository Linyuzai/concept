package com.github.linyuzai.inherit.core.flag;

/**
 * 继承标识
 * <p>
 * 用于扩展额外功能
 */
public enum InheritFlag {

    /**
     * 继承字段时有效
     * <p>
     * 根据字段生成 builder 对应的方法
     */
    BUILDER,

    /**
     * 继承字段时有效
     * <p>
     * 根据字段生成 getter 对应的方法
     */
    GETTER,

    /**
     * 继承字段时有效
     * <p>
     * 根据字段生成 setter 对应的方法
     */
    SETTER,

    /**
     * 表示同时处理自身字段和方法
     * <p>
     * 搭配其他标识使用
     */
    OWN
}
