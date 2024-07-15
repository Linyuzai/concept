package com.github.linyuzai.plugin.core.type;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 嵌套类型
 * <p>
 * 以 List&lt;? extends Class&lt;? extends A&gt;[]&gt; 为例
 * <blockquote><pre>
 * {
 *     toType: List&lt;? extends Class&lt;? extends A&gt;[]&gt;,
 *     toClass: List,
 *     children: [{
 *          toType: Class&lt;? extends A&gt;[],
 *          toClass: Class[],
 *          children: [{
 *              toType: Class&lt;? extends A&gt;,
 *              toClass: Class,
 *              children: [{
 *                  toType: A,
 *                  toClass: A,
 *                  children: [{
 *                  }]
 *              }]
 *          }]
 *     }]
 * }
 * </pre></blockquote>
 */
public interface NestedType {

    Type toType();

    Class<?> toClass();

    /**
     * 获得父类型
     */
    NestedType getParent();

    /**
     * 获得子类型
     */
    List<NestedType> getChildren();
}
