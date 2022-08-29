package com.github.linyuzai.inherit.core.handler;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

public interface InheritHandler {

    void handle(JCTree tree, JCTree.JCClassDecl targetClass, TreeMaker treeMaker, Names names);
}
