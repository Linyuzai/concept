package com.github.linyuzai.inherit.core;

import com.google.auto.service.AutoService;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.*;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class InheritFieldsProcessor extends AbstractProcessor {

    private static final String ANNOTATION_NAME = "com.github.linyuzai.inherit.core.InheritFields";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        TreeMaker treeMaker = TreeMaker.instance(context);
        Names names = Names.instance(context);
        JavacElements elementUtils = (JavacElements) processingEnv.getElementUtils();
        //扫描到的 @BuilderRef
        for (TypeElement annotation : annotations) {
            //获得标注了 @BuilderRef 的 Builder 类
            Set<? extends Element> builderElements = roundEnv.getElementsAnnotatedWith(annotation);
            //遍历这些 Builder 类
            for (Element builderElement : builderElements) {
                //只有在类上生效
                if (!isClass(builderElement)) {
                    continue;
                }
                //Builder 的类型
                Type builderType = ((Symbol.ClassSymbol) builderElement).type;
                //获得 Builder 的语法树
                JCTree.JCClassDecl builderTree = (JCTree.JCClassDecl) elementUtils.getTree(builderElement);
                //遍历 Builder 上的注解
                for (AnnotationMirror builderRefAnnotation : builderElement.getAnnotationMirrors()) {
                    //忽略 @BuilderRef
                    if (!isSupportAnnotation(builderRefAnnotation)) {
                        continue;
                    }
                    //读取 @BuilderRef 的参数
                    for (AnnotationValue refValue : builderRefAnnotation.getElementValues().values()) {
                        //获得指定的 Class
                        TypeMirror refType = (TypeMirror) refValue.getValue();
                        if (!isClass(refType)) {
                            continue;
                        }

                        Type.ClassType refClassType = (Type.ClassType) refType;
                        //获得指定 Class 的语法树
                        JCTree.JCClassDecl refClass = (JCTree.JCClassDecl) elementUtils
                                .getTree(refClassType.asElement());

                        List<JCTree.JCExpression> typeargs = List.nil();
                        List<JCTree.JCExpression> args = List.nil();

                        //遍历所有的属性，接口等
                        for (JCTree refDef : refClass.defs) {
                            //忽略非字段类型的定义
                            if (!isVariable(refDef)) {
                                continue;
                            }

                            JCTree.JCVariableDecl refVariable = (JCTree.JCVariableDecl) refDef;

                            //静态字段忽略
                            if (refVariable.mods.getFlags().contains(Modifier.STATIC)) {
                                continue;
                            }

                            //用属性名称作为方法名称
                            Name name = refVariable.name;
                            args = args.append(treeMaker.Ident(name));

                            //字段类型
                            JCTree.JCExpression vartype = refVariable.vartype;
                            typeargs = typeargs.append(vartype);

                            //把字段添加到 Builder 类上
                            JCTree.JCVariableDecl varDef = treeMaker.VarDef(treeMaker.Modifiers(Flags.PROTECTED),
                                    name, vartype, null);
                            builderTree.defs = builderTree.defs.append(varDef);

                            //返回值类型为 Builder 类型
                            JCTree.JCExpression returnType = treeMaker.Type(builderType);
                            //入参用属性名称和属性类型
                            JCTree.JCVariableDecl variableDecl = treeMaker.VarDef(
                                    treeMaker.Modifiers(Flags.PARAMETER),
                                    name, vartype, null);
                            List<JCTree.JCVariableDecl> variableDecls = List.of(variableDecl);
                            //方法表达式
                            ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
                            //this.xxx = xxx;
                            JCTree.JCExpressionStatement assign = treeMaker.Exec(
                                    treeMaker.Assign(
                                            treeMaker.Select(
                                                    treeMaker.Ident(names.fromString("this")), name),
                                            treeMaker.Ident(name)
                                    )
                            );
                            statements.add(assign);
                            //return this;
                            JCTree.JCStatement ret = treeMaker.Return(
                                    treeMaker.Ident(names.fromString("this")));
                            statements.add(ret);
                            //方法代码块
                            JCTree.JCBlock block = treeMaker.Block(0, statements.toList());
                            //泛型字段
                            List<JCTree.JCTypeParameter> typeParameters = List.nil();
                            //异常
                            List<JCTree.JCExpression> throwsClauses = List.nil();
                            //生成方法
                            JCTree.JCMethodDecl methodDecl = treeMaker
                                    .MethodDef(treeMaker.Modifiers(Flags.PUBLIC), name, returnType,
                                            typeParameters, variableDecls, throwsClauses,
                                            block, null);
                            //添加方法
                            builderTree.defs = builderTree.defs.append(methodDecl);
                        }

                        //build 方法
                        //X $$ = new X();
                        JCTree.JCNewClass newClass = treeMaker.NewClass(
                                null,
                                typeargs,
                                treeMaker.Type(refClassType),
                                args,
                                null
                        );
                        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
                        JCTree.JCVariableDecl build = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER),
                                names.fromString("_$build"), treeMaker.Type(refClassType),
                                newClass);
                        statements.add(build);
                        //return $$;
                        JCTree.JCStatement ret = treeMaker.Return(
                                treeMaker.Ident(names.fromString("_$build")));
                        statements.add(ret);
                        //方法代码块
                        JCTree.JCBlock block = treeMaker.Block(0, statements.toList());
                        JCTree.JCMethodDecl methodDecl = treeMaker
                                .MethodDef(treeMaker.Modifiers(Flags.PUBLIC),
                                        names.fromString("build"),
                                        treeMaker.Type(refClassType.asElement().type),
                                        List.nil(), List.nil(), List.nil(),
                                        block, null);
                        //添加 build 方法
                        builderTree.defs = builderTree.defs.append(methodDecl);
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
