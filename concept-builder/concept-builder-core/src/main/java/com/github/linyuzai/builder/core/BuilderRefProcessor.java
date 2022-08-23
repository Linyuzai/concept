package com.github.linyuzai.builder.core;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.Name;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@SupportedAnnotationTypes(BuilderProcessor.ANNOTATION_NAME)
public class BuilderRefProcessor extends AbstractProcessor {

    private static final String ANNOTATION_NAME = "com.github.linyuzai.builder.core.BuilderRef";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        TreeMaker treeMaker = TreeMaker.instance(context);
        Names names = Names.instance(context);
        JavacElements elementUtils = (JavacElements) processingEnv.getElementUtils();
        //扫描到的 @BuilderRef
        for (TypeElement annotation : annotations) {
            //获得标注了 @BuilderRef 的 Builder 类
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            //遍历这些 Builder 类
            for (Element element : elements) {
                //只有在类上生效
                if (!isClass(element)) {
                    continue;
                }
                //Builder 的类型
                Type builderType = ((Symbol.ClassSymbol) element).type;
                //获得 Builder 的语法树
                JCTree.JCClassDecl tree = (JCTree.JCClassDecl) elementUtils.getTree(element);

                for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
                    if (!isSupportAnnotation(mirror)) {
                        continue;
                    }
                    for (AnnotationValue value : mirror.getElementValues().values()) {
                        TypeMirror clazz = (TypeMirror) value.getValue();
                        if (!isClass(clazz)) {
                            continue;
                        }
                        Type.ClassType classType = (Type.ClassType) clazz;
                        JCTree.JCClassDecl refClass = (JCTree.JCClassDecl) elementUtils
                                .getTree(classType.asElement());
                        for (JCTree def : refClass.defs) {
                            if (!isVariable(def)) {
                                continue;
                            }
                            tree.defs = tree.defs.append(def);
                            JCTree.JCVariableDecl refVariable = (JCTree.JCVariableDecl) def;
                            Name name = refVariable.name;
                            JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
                            JCTree.JCExpression returnType = treeMaker.Type(builderType);
                            //变量
                            JCTree.JCVariableDecl variableDecl = treeMaker.VarDef(
                                    treeMaker.Modifiers(Flags.PARAMETER),
                                    name, refVariable.vartype, null);
                            List<JCTree.JCVariableDecl> variableDecls = List.of(variableDecl);
                            ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
                            JCTree.JCExpressionStatement assign = treeMaker.Exec(
                                    treeMaker.Assign(treeMaker.Select(
                                                    treeMaker.Ident(names.fromString("this")), name),
                                            treeMaker.Ident(name)));
                            statements.add(assign);
                            JCTree.JCStatement ret = treeMaker.Return(
                                    treeMaker.Ident(names.fromString("this")));
                            statements.add(ret);
                            JCTree.JCBlock block = treeMaker.Block(0, statements.toList());
                            List<JCTree.JCTypeParameter> typeParameters = List.nil();
                            List<JCTree.JCExpression> throwsClauses = List.nil();
                            JCTree.JCMethodDecl methodDecl = treeMaker
                                    .MethodDef(modifiers, name, returnType,
                                            typeParameters, variableDecls, throwsClauses,
                                            block, null);
                            tree.defs = tree.defs.append(methodDecl);
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean isClass(Element element) {
        //return element.getKind().isClass();
        return element instanceof Symbol.ClassSymbol;
    }

    private boolean isClass(TypeMirror mirror) {
        return mirror instanceof Type.ClassType;
    }

    private boolean isVariable(JCTree tree) {
        return tree instanceof JCTree.JCVariableDecl;
    }

    private boolean isSupportAnnotation(AnnotationMirror mirror) {
        DeclaredType declaredType = mirror.getAnnotationType();
        if (isClass(declaredType)) {
            Symbol.TypeSymbol typeSymbol = ((Type.ClassType) declaredType).tsym;
            if (isClass(typeSymbol)) {
                return ANNOTATION_NAME.equals(((Symbol.ClassSymbol) typeSymbol).fullname.toString());
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(ANNOTATION_NAME);
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
}
