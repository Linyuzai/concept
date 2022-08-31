package com.github.linyuzai.inherit.processor.handler;

import com.github.linyuzai.inherit.core.flag.InheritFlag;
import com.github.linyuzai.inherit.core.handler.InheritHandler;
import com.github.linyuzai.inherit.processor.utils.InheritUtils;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import java.util.Collection;

/**
 * 根据字段生成方法处理器
 */
public class GenerateMethodsWithFieldsHandler implements InheritHandler {

    /**
     * 是否生成 builder 方法
     */
    private final boolean builder;

    /**
     * 是否生成 getter 方法
     */
    private final boolean getter;

    /**
     * 是否生成 setter 方法
     */
    private final boolean setter;

    public GenerateMethodsWithFieldsHandler(Collection<String> flags) {
        builder = flags.contains(InheritFlag.BUILDER.name());
        getter = flags.contains(InheritFlag.GETTER.name());
        setter = flags.contains(InheritFlag.SETTER.name());
    }

    @Override
    public void handle(JCTree tree, JCTree.JCClassDecl targetClass, TreeMaker treeMaker, Names names, int level) {
        //只处理字段
        if (tree instanceof JCTree.JCVariableDecl) {
            JCTree.JCVariableDecl refVariable = (JCTree.JCVariableDecl) tree;
            //字段名称
            Name name = refVariable.name;
            //字段类型
            JCTree.JCExpression vartype = refVariable.vartype;
            //生成 builder 方法
            if (builder) {
                //返回值类型为 对应类 类型
                JCTree.JCExpression returnType = treeMaker.Type(targetClass.sym.asType());
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
                //方法泛型
                List<JCTree.JCTypeParameter> typeParameters = List.nil();
                //方法异常
                List<JCTree.JCExpression> throwsClauses = List.nil();
                //生成方法
                JCTree.JCMethodDecl builderMethodDecl = treeMaker
                        .MethodDef(treeMaker.Modifiers(Flags.PUBLIC), name, returnType,
                                typeParameters, variableDecls, throwsClauses,
                                block, null);
                //如果未定义
                if (!InheritUtils.isMethodDefined(targetClass, builderMethodDecl)) {
                    //添加方法
                    targetClass.defs = targetClass.defs.append(builderMethodDecl);
                }
            }
            //生成 getter 方法
            if (getter) {
                //返回值类型为 对应字段 类型
                JCTree.JCExpression returnType = vartype;
                //无入参
                List<JCTree.JCVariableDecl> variableDecls = List.nil();
                //方法表达式
                //return this.xxx;
                JCTree.JCStatement ret = treeMaker.Return(treeMaker.Select(
                        treeMaker.Ident(names.fromString("this")), name));
                //方法代码块
                JCTree.JCBlock block = treeMaker.Block(0, List.of(ret));
                //方法泛型
                List<JCTree.JCTypeParameter> typeParameters = List.nil();
                //方法异常
                List<JCTree.JCExpression> throwsClauses = List.nil();
                //方法名称
                String fieldName = name.toString();
                String prefix;
                if (vartype.type instanceof Type.JCPrimitiveType && vartype.type.hasTag(TypeTag.BOOLEAN)) {
                    prefix = "is";
                } else {
                    prefix = "get";
                }
                String methodName = prefix + fieldName.substring(0, 1).toUpperCase() +
                        fieldName.substring(1);
                //生成方法
                JCTree.JCMethodDecl getterMethodDecl = treeMaker
                        .MethodDef(treeMaker.Modifiers(Flags.PUBLIC),
                                names.fromString(methodName),
                                returnType, typeParameters, variableDecls,
                                throwsClauses, block, null);
                //如果未定义
                if (!InheritUtils.isMethodDefined(targetClass, getterMethodDecl)) {
                    //添加方法
                    targetClass.defs = targetClass.defs.append(getterMethodDecl);
                }
            }
            //生成 setter 方法
            if (setter) {
                //返回值类型为 Void 类型
                JCTree.JCExpression returnType = treeMaker.TypeIdent(TypeTag.VOID);
                //入参用属性名称和属性类型
                JCTree.JCVariableDecl variableDecl = treeMaker.VarDef(
                        treeMaker.Modifiers(Flags.PARAMETER),
                        name, vartype, null);
                List<JCTree.JCVariableDecl> variableDecls = List.of(variableDecl);
                //方法表达式
                //this.xxx = xxx;
                JCTree.JCExpressionStatement assign = treeMaker.Exec(
                        treeMaker.Assign(
                                treeMaker.Select(
                                        treeMaker.Ident(names.fromString("this")), name),
                                treeMaker.Ident(name)
                        )
                );
                //方法代码块
                JCTree.JCBlock block = treeMaker.Block(0, List.of(assign));
                //方法泛型
                List<JCTree.JCTypeParameter> typeParameters = List.nil();
                //方法异常
                List<JCTree.JCExpression> throwsClauses = List.nil();
                //方法名称
                String fieldName = name.toString();
                String methodName = "set" + fieldName.substring(0, 1).toUpperCase() +
                        fieldName.substring(1);
                //生成方法
                JCTree.JCMethodDecl setterMethodDecl = treeMaker
                        .MethodDef(treeMaker.Modifiers(Flags.PUBLIC),
                                names.fromString(methodName),
                                returnType, typeParameters, variableDecls,
                                throwsClauses, block, null);
                //如果未定义
                if (!InheritUtils.isMethodDefined(targetClass, setterMethodDecl)) {
                    //添加方法
                    targetClass.defs = targetClass.defs.append(setterMethodDecl);
                }
            }
        }
    }
}
