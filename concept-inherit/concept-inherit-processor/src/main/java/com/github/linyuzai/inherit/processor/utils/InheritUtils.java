package com.github.linyuzai.inherit.processor.utils;

import com.github.linyuzai.inherit.core.flag.InheritFlags;
import com.github.linyuzai.inherit.processor.handler.GenerateMethodsWithFieldsHandler;
import com.github.linyuzai.inherit.core.handler.InheritHandler;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Collection;

public class InheritUtils {

    public static Collection<InheritHandler> getInheritHandlers(Collection<String> flags) {
        Collection<String> of = InheritFlags.of(flags);
        Collection<InheritHandler> handlers = new ArrayList<>();
        if (of.contains(InheritFlags.GENERATE_METHODS_WITH_FIELDS)) {
            handlers.add(new GenerateMethodsWithFieldsHandler(of));
        }
        return handlers;
    }

    public static boolean isAnnotation(AnnotationMirror annotation, String className) {
        DeclaredType declaredType = annotation.getAnnotationType();
        if (isClass(declaredType)) {
            Symbol.TypeSymbol typeSymbol = ((Type.ClassType) declaredType).tsym;
            if (isClass(typeSymbol)) {
                return className.equals(((Symbol.ClassSymbol) typeSymbol).fullname.toString());
            }
        }
        return false;
    }

    public static boolean isClass(Element element) {
        //return element.getKind().isClass();
        return element instanceof Symbol.ClassSymbol;
    }

    public static boolean isClass(TypeMirror mirror) {
        return mirror instanceof Type.ClassType;
    }

    public static boolean isFieldDefined(JCTree.JCClassDecl classDef, JCTree.JCVariableDecl varDef) {
        for (JCTree def : classDef.defs) {
            if (isNonStaticVariable(def)) {
                JCTree.JCVariableDecl has = (JCTree.JCVariableDecl) def;
                if (isVariableEqual(has, varDef)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isMethodDefined(JCTree.JCClassDecl classDef, JCTree.JCMethodDecl methodDef) {
        for (JCTree def : classDef.defs) {
            if (isNonStaticMethod(def)) {
                JCTree.JCMethodDecl has = (JCTree.JCMethodDecl) def;
                if (isMethodEqual(has, methodDef)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNonStaticVariable(JCTree tree) {
        return tree instanceof JCTree.JCVariableDecl &&
                !((JCTree.JCVariableDecl) tree).mods.getFlags().contains(Modifier.STATIC);
    }

    public static boolean isNonStaticMethod(JCTree tree) {
        return tree instanceof JCTree.JCMethodDecl &&
                !"<init>".equals(((JCTree.JCMethodDecl) tree).getName().toString()) &&
                !((JCTree.JCMethodDecl) tree).mods.getFlags().contains(Modifier.STATIC);
    }

    public static boolean isMethodEqual(JCTree.JCMethodDecl methodDef1, JCTree.JCMethodDecl methodDef2) {
        return methodDef1.name.contentEquals(methodDef2.name) &&
                methodDef1.mods.getFlags().equals(methodDef2.mods.getFlags()) &&
                methodDef1.restype.type.equals(methodDef2.restype.type) &&
                isVariablesEqual(methodDef1.params, methodDef2.params);
    }

    public static boolean isVariablesEqual(List<JCTree.JCVariableDecl> varDef1s, List<JCTree.JCVariableDecl> varDef2s) {
        if (varDef1s.size() != varDef2s.size()) {
            return false;
        }
        for (int i = 0; i < varDef1s.size(); i++) {
            if (!isVariableEqual(varDef1s.get(i), varDef2s.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isVariableEqual(JCTree.JCVariableDecl varDef1, JCTree.JCVariableDecl varDef2) {
        return varDef1.name.contentEquals(varDef2.name) &&
                varDef1.mods.getFlags().equals(varDef2.mods.getFlags()) &&
                varDef1.vartype.type.equals(varDef2.vartype.type);
    }
}
