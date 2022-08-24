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
                //遍历 Builder 上的注解
                for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
                    //忽略 @BuilderRef
                    if (!isSupportAnnotation(mirror)) {
                        continue;
                    }
                    //读取 @BuilderRef 的参数
                    for (AnnotationValue value : mirror.getElementValues().values()) {
                        //获得指定的 Class
                        TypeMirror clazz = (TypeMirror) value.getValue();
                        if (!isClass(clazz)) {
                            continue;
                        }

                        Type.ClassType classType = (Type.ClassType) clazz;
                        //获得指定 Class 的语法树
                        JCTree.JCClassDecl refClass = (JCTree.JCClassDecl) elementUtils
                                .getTree(classType.asElement());

                        List<JCTree.JCExpression> typeargs = List.nil();
                        List<JCTree.JCExpression> args = List.nil();

                        //遍历所有的属性，接口等
                        for (JCTree def : refClass.defs) {
                            //忽略非字段类型的定义
                            if (!isVariable(def)) {
                                continue;
                            }

                            JCTree.JCVariableDecl refVariable = (JCTree.JCVariableDecl) def;

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
                            tree.defs = tree.defs.append(varDef);

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
                            tree.defs = tree.defs.append(methodDecl);
                        }

                        //build 方法
                        //X $$ = new X();
                        JCTree.JCNewClass newClass = treeMaker.NewClass(
                                null,
                                typeargs,
                                treeMaker.Type(classType),
                                args,
                                null
                        );
                        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
                        JCTree.JCVariableDecl build = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER),
                                names.fromString("_$build"), treeMaker.Type(classType),
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
                                        treeMaker.Type(classType.asElement().type),
                                        List.nil(), List.nil(), List.nil(),
                                        block, null);
                        //添加 build 方法
                        tree.defs = tree.defs.append(methodDecl);
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
