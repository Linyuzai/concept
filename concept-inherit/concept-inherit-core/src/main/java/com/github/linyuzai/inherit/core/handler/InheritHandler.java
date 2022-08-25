package com.github.linyuzai.inherit.core.handler;

import com.github.linyuzai.inherit.core.flag.InheritFlags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import java.util.ArrayList;
import java.util.Collection;

public interface InheritHandler {

    void handle(JCTree tree, JCTree.JCClassDecl targetClass, TreeMaker treeMaker, Names names);

    static Collection<InheritHandler> from(Collection<String> flags) {
        Collection<String> of = InheritFlags.of(flags);
        Collection<InheritHandler> handlers = new ArrayList<>();
        if (of.contains(InheritFlags.GENERATE_METHODS_WITH_FIELDS)) {
            handlers.add(new GenerateMethodsWithFieldsHandler(of));
        }
        return handlers;
    }
}
