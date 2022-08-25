package com.github.linyuzai.inherit.core.processor;

import com.github.linyuzai.inherit.core.handler.InheritHandler;
import com.github.linyuzai.inherit.core.utils.InheritUtils;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.model.JavacElements;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.*;
import com.sun.tools.javac.util.List;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import java.util.*;

@SuppressWarnings("unchecked")
public abstract class AbstractInheritProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        TreeMaker treeMaker = TreeMaker.instance(context);
        Names names = Names.instance(context);
        JavacElements elementUtils = (JavacElements) processingEnv.getElementUtils();
        //扫描到的 @Inherit
        for (TypeElement annotation : annotations) {
            //获得标注了 @Inherit 的类
            Set<? extends Element> targetClassElements = roundEnv.getElementsAnnotatedWith(annotation);
            //遍历这些类
            for (Element targetClassElement : targetClassElements) {
                //只有在类上生效
                if (!InheritUtils.isClass(targetClassElement)) {
                    continue;
                }
                //获得 Target类 的语法树
                JCTree.JCClassDecl targetClassDef = (JCTree.JCClassDecl) elementUtils.getTree(targetClassElement);
                //获得 Target类 上的 @Inherit 注解
                Collection<AnnotationMirror> annotationsWithRepeat = getAnnotationsWithRepeat(targetClassElement);
                //遍历 @Inherit 注解
                for (AnnotationMirror inheritAnnotation : annotationsWithRepeat) {
                    Map<String, Object> attributes = getAttributes(inheritAnnotation);
                    Collection<Type.ClassType> sources = convertClass((Collection<Attribute.Class>) attributes
                            .get("sources"));
                    Boolean inheritSuper = (Boolean) attributes
                            .getOrDefault("inheritSuper", true);
                    Collection<String> excludeFields = convertString((Collection<Attribute.Constant>) attributes
                            .getOrDefault("excludeFields", Collections.emptyList()));
                    Collection<String> excludeMethods = convertString((Collection<Attribute.Constant>) attributes
                            .getOrDefault("excludeMethods", Collections.emptyList()));
                    Collection<String> flags = convertString((Collection<Attribute.Constant>) attributes
                            .getOrDefault("flags", Collections.emptyList()));

                    Collection<InheritHandler> handlers = InheritHandler.from(flags);

                    for (Type.ClassType sourceClass : sources) {
                        //获得指定 Class 的语法树
                        inheritClass(sourceClass, targetClassDef, inheritSuper,
                                excludeFields, excludeMethods, handlers, treeMaker, names, elementUtils);
                    }
                }
            }
        }
        return true;
    }

    protected boolean inheritFields() {
        return false;
    }

    protected boolean inheritMethods() {
        return false;
    }

    protected abstract String getAnnotationName();

    protected abstract String getRepeatableAnnotationName();

    private Collection<AnnotationMirror> getAnnotationsWithRepeat(Element element) {
        Collection<AnnotationMirror> annotations = new ArrayList<>();
        for (AnnotationMirror annotation : element.getAnnotationMirrors()) {
            if (InheritUtils.isAnnotation(annotation, getAnnotationName())) {
                annotations.add(annotation);
            } else if (InheritUtils.isAnnotation(annotation, getRepeatableAnnotationName())) {
                for (AnnotationValue value : annotation.getElementValues().values()) {
                    List<AnnotationMirror> children = (List<AnnotationMirror>) value.getValue();
                    annotations.addAll(children);
                }
            }
        }
        return annotations;
    }

    private Map<String, Object> getAttributes(AnnotationMirror annotation) {
        Map<String, Object> attributes = new LinkedHashMap<>();
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
                annotation.getElementValues().entrySet()) {
            Symbol.MethodSymbol key = (Symbol.MethodSymbol) entry.getKey();
            attributes.put(key.getQualifiedName().toString(), entry.getValue().getValue());
        }
        return attributes;
    }

    private Collection<Type.ClassType> convertClass(Collection<Attribute.Class> classes) {
        Collection<Type.ClassType> classTypes = new ArrayList<>();
        for (Attribute.Class attribute : classes) {
            classTypes.add((Type.ClassType) attribute.getValue());
        }
        return classTypes;
    }

    private Collection<String> convertString(Collection<Attribute.Constant> constants) {
        Collection<String> set = new HashSet<>();
        for (Attribute.Constant constant : constants) {
            set.add(constant.value.toString());
        }
        return set;
    }

    private void inheritClass(Type.ClassType sourceClass,
                              JCTree.JCClassDecl targetClassDef,
                              Boolean inheritSuper,
                              Collection<String> excludeFields,
                              Collection<String> excludeMethods,
                              Collection<InheritHandler> handlers,
                              TreeMaker treeMaker,
                              Names names,
                              JavacElements elementUtils) {
        JCTree.JCClassDecl sourceClassDef = (JCTree.JCClassDecl) elementUtils
                .getTree(sourceClass.asElement());
        if (inheritSuper) {
            Symbol.TypeSymbol symbol = sourceClass.supertype_field.tsym;
            if (symbol == null) {
                return;
            }
            Type type = symbol.asType();
            if (type instanceof Type.ClassType) {
                inheritClass((Type.ClassType) type, targetClassDef, true,
                        excludeFields, excludeMethods, handlers, treeMaker, names, elementUtils);
            }
        }
        for (JCTree sourceDef : sourceClassDef.defs) {
            if (inheritFields() && InheritUtils.isNonStaticVariable(sourceDef)) {
                JCTree.JCVariableDecl sourceVarDef = (JCTree.JCVariableDecl) sourceDef;
                if (!excludeFields.contains(sourceVarDef.name.toString())) {
                    if (!InheritUtils.isFieldDefined(targetClassDef, sourceVarDef)) {
                        targetClassDef.defs = targetClassDef.defs.append(sourceVarDef);
                    }
                    for (InheritHandler handler : handlers) {
                        handler.handle(sourceDef, targetClassDef, treeMaker, names);
                    }
                }
            }
            if (inheritMethods() && InheritUtils.isNonStaticMethod(sourceDef)) {
                JCTree.JCMethodDecl sourceMethodDef = (JCTree.JCMethodDecl) sourceDef;
                if (!excludeMethods.contains(sourceMethodDef.name.toString())) {
                    if (!InheritUtils.isMethodDefined(targetClassDef, sourceMethodDef)) {
                        targetClassDef.defs = targetClassDef.defs.append(sourceMethodDef);
                    }
                    for (InheritHandler handler : handlers) {
                        handler.handle(sourceDef, targetClassDef, treeMaker, names);
                    }
                }
            }
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Arrays.asList(getAnnotationName(), getRepeatableAnnotationName()));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }
}
