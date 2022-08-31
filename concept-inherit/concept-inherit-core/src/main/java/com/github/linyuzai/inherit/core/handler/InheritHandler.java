package com.github.linyuzai.inherit.core.handler;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

/**
 * 继承处理器
 * <p>
 * 用于扩展基于标识的逻辑处理
 */
public interface InheritHandler {

    /**
     * 执行处理
     *
     * @param tree        字段或方法
     * @param targetClass 所属的类
     * @param treeMaker   节点工具
     * @param names       名称工具
     * @param level       层级
     */
    void handle(JCTree tree, JCTree.JCClassDecl targetClass, TreeMaker treeMaker, Names names, int level);
}
