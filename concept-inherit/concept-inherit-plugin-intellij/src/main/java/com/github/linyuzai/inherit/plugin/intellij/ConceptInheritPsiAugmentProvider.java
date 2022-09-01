package com.github.linyuzai.inherit.plugin.intellij;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import com.intellij.psi.impl.light.*;
import com.intellij.psi.impl.source.PsiExtensibleClass;
import com.intellij.psi.impl.source.tree.java.PsiIdentifierImpl;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class ConceptInheritPsiAugmentProvider extends PsiAugmentProvider {

    private final Set<String> FIELD_ANNOTATION_NAMES = new HashSet<>(Arrays.asList(
            "com.github.linyuzai.inherit.core.annotation.InheritClass",
            "com.github.linyuzai.inherit.core.annotation.InheritField"));

    private final Set<String> FIELD_REPEATABLE_ANNOTATION_NAMES = new HashSet<>(Arrays.asList(
            "com.github.linyuzai.inherit.core.annotation.InheritClasses",
            "com.github.linyuzai.inherit.core.annotation.InheritFields"));

    private final Set<String> METHOD_ANNOTATION_NAMES = new HashSet<>(Arrays.asList(
            "com.github.linyuzai.inherit.core.annotation.InheritClass",
            "com.github.linyuzai.inherit.core.annotation.InheritMethod"));

    private final Set<String> METHOD_REPEATABLE_ANNOTATION_NAMES = new HashSet<>(Arrays.asList(
            "com.github.linyuzai.inherit.core.annotation.InheritClasses",
            "com.github.linyuzai.inherit.core.annotation.InheritMethods"));

    @Override
    protected @NotNull <Psi extends PsiElement> List<Psi> getAugments(@NotNull PsiElement element, @NotNull Class<Psi> type) {
        List<Psi> psis = getPsis(element, type, new HashSet<>(), new HashSet<>(), null, null);
        return psis == null ? super.getAugments(element, type) : psis;
    }

    /**
     * 获得所有的字段或方法
     *
     * @param element                目标类
     * @param type                   字段或方法类型
     * @param hasHandleFieldClasses  递归处理时避免再次处理已经处理过的类
     * @param hasHandleMethodClasses 递归处理时避免再次处理已经处理过的类
     * @param fieldFlagsConsumer     处理字段的 flags
     * @param methodFlagsConsumer    处理方法的 flags
     * @param <Psi>                  字段或方法类型
     */
    @SuppressWarnings("unchecked")
    private <Psi extends PsiElement> List<Psi> getPsis(@NotNull PsiElement element, @NotNull Class<Psi> type,
                                                       Collection<String> hasHandleFieldClasses,
                                                       Collection<String> hasHandleMethodClasses,
                                                       BiConsumer<PsiField, Collection<String>> fieldFlagsConsumer,
                                                       BiConsumer<PsiMethod, Collection<String>> methodFlagsConsumer) {
        //不存在指定依赖直接返回
        if (!LibraryUtils.hasLibrary(element.getProject())) {
            return null;
        }
        //只处理 Class
        if (!(element instanceof PsiClass)) {
            return null;
        }

        List<PsiElement> list = new ArrayList<>();
        //要处理的目标 Class
        PsiClass targetClass = (PsiClass) element;
        PsiManager manager = targetClass.getManager();
        //需要获得字段
        if (type.isAssignableFrom(PsiField.class)) {
            //如果已经处理过则直接返回空列表
            if (hasHandleFieldClasses.contains(targetClass.getQualifiedName())) {
                return Collections.emptyList();
            }
            //将目标 Class 标记为字段已处理
            hasHandleFieldClasses.add(targetClass.getQualifiedName());
            //获得所有继承字段的注解
            Collection<PsiAnnotation> annotations = findFieldAnnotations(targetClass);
            //目标类已经定义的字段，不包括本功能生成的字段
            Collection<PsiField> hasFields = collectClassFieldsIntern(targetClass);
            //遍历注解
            for (PsiAnnotation annotation : annotations) {
                //需要继承的 Class
                Collection<PsiType> sources = findTypes(annotation.findAttributeValue("sources"));
                //是否处理父类
                Boolean inheritSuper = getBoolean(annotation.findAttributeValue("inheritSuper"));
                //需要排除的字段
                Collection<String> excludeFields = findStrings(annotation.findAttributeValue("excludeFields"));
                //需要处理的 flags
                Collection<String> flags = InheritFlag.of(findEnums(annotation.findAttributeValue("flags")));
                //遍历需要继承的 Class
                for (PsiType sourceType : sources) {
                    //获得对应的 Class
                    PsiClass sourceClass = PsiUtil.resolveClassInType(sourceType);
                    if (sourceClass == null) {
                        continue;
                    }
                    //获得所有的字段，包括本功能生成的字段
                    Collection<PsiField> fields =
                            getFields(sourceClass, inheritSuper, hasHandleFieldClasses, hasHandleMethodClasses);
                    //遍历字段
                    for (PsiField field : fields) {
                        String fieldName = field.getName();
                        //忽略静态字段
                        //忽略排除的字段
                        //忽略已经定义的字段
                        if (field.hasModifierProperty(PsiModifier.STATIC) ||
                                excludeFields.contains(fieldName) ||
                                hasFieldDefined(field, hasFields)) {
                            continue;
                        }
                        LightFieldBuilder fieldBuilder =
                                new LightFieldBuilder(manager,
                                        fieldName,
                                        field.getType());
                        //访问限定
                        fieldBuilder.setModifierList(new LightModifierList(field));
                        //初始化
                        fieldBuilder.setInitializer(field.getInitializer());
                        //所属的Class
                        fieldBuilder.setContainingClass(targetClass);
                        //是否 Deprecated
                        fieldBuilder.setIsDeprecated(field.isDeprecated());
                        //注释
                        fieldBuilder.setDocComment(field.getDocComment());
                        //导航
                        fieldBuilder.setNavigationElement(field);
                        //添加字段
                        list.add(fieldBuilder);
                        //回调字段和对应的 flags
                        if (fieldFlagsConsumer != null) {
                            fieldFlagsConsumer.accept(field, flags);
                        }
                    }
                }
            }
        }
        //需要获得方法
        if (type.isAssignableFrom(PsiMethod.class)) {
            //如果已经处理过则直接返回空列表
            if (hasHandleMethodClasses.contains(targetClass.getQualifiedName())) {
                return Collections.emptyList();
            }
            //将目标 Class 标记为方法已处理
            hasHandleMethodClasses.add(targetClass.getQualifiedName());
            //获得所有继承方法的注解
            Collection<PsiAnnotation> annotations = findMethodAnnotations(targetClass);
            //目标类已经定义的方法，不包括本功能生成的方法
            Collection<PsiMethod> hasMethods = collectClassMethodsIntern(targetClass);
            //遍历需要继承的 Class
            for (PsiAnnotation annotation : annotations) {
                //需要继承的 Class
                Collection<PsiType> sources = findTypes(annotation.findAttributeValue("sources"));
                //是否处理父类
                Boolean inheritSuper = getBoolean(annotation.findAttributeValue("inheritSuper"));
                //需要排除的方法
                Collection<String> excludeMethods = findStrings(annotation.findAttributeValue("excludeMethods"));
                //需要处理的 flags
                Collection<String> flags = InheritFlag.of(findEnums(annotation.findAttributeValue("flags")));
                //遍历需要继承的 Class
                for (PsiType sourceType : sources) {
                    //获得对应的 Class
                    PsiClass sourceClass = PsiUtil.resolveClassInType(sourceType);
                    if (sourceClass == null) {
                        continue;
                    }
                    //获得所有的方法，包括本功能生成的方法
                    Collection<PsiMethod> methods =
                            getMethods(sourceClass, inheritSuper, hasHandleFieldClasses, hasHandleMethodClasses);
                    //遍历方法
                    for (PsiMethod method : methods) {
                        //忽略静态方法
                        //忽略排除的方法
                        //忽略已经定义的方法
                        if (method.hasModifierProperty(PsiModifier.STATIC) ||
                                excludeMethods.contains(method.getName()) ||
                                hasMethodDefined(method, hasMethods)) {
                            continue;
                        }
                        LightMethodBuilder methodBuilder =
                                new LightMethodBuilder(manager,
                                        JavaLanguage.INSTANCE,
                                        method.getName(),
                                        method.getParameterList(),
                                        method.getModifierList(),
                                        method.getThrowsList(),
                                        method.getTypeParameterList());
                        //返回值
                        methodBuilder.setMethodReturnType(method.getReturnType());
                        //所属的 Class
                        methodBuilder.setContainingClass(targetClass);
                        //导航
                        methodBuilder.setNavigationElement(method);
                        //添加方法
                        list.add(methodBuilder);
                        //回调方法和对应的 flags
                        if (methodFlagsConsumer != null) {
                            methodFlagsConsumer.accept(method, flags);
                        }
                    }
                }
            }

            //自身字段的 flags
            Set<String> ownFlags = new HashSet<>();
            //回调处理对应，添加需要生成的方法
            BiConsumer<PsiField, Collection<String>> fieldConsumer = (field, flags) -> {
                if (InheritFlag.hasFlag(flags)) {
                    String fieldName = field.getName();
                    //需要生成 builder 方法
                    if (flags.contains(InheritFlag.BUILDER.name())) {
                        //是否需要生成自身的字段对应的方法
                        if (flags.contains(InheritFlag.OWN.name())) {
                            ownFlags.add(InheritFlag.BUILDER.name());
                        }
                        LightMethodBuilder methodBuilder =
                                new LightMethodBuilder(manager,
                                        JavaLanguage.INSTANCE,
                                        field.getName());
                        //public
                        methodBuilder.setModifiers(PsiModifier.PUBLIC);
                        //入参使用字段名和字段类型
                        methodBuilder.addParameter(fieldName, field.getType());
                        //返回值为目标 Class
                        methodBuilder.setMethodReturnType(PsiTypesUtil.getClassType(targetClass));
                        //所属的 Class
                        methodBuilder.setContainingClass(targetClass);
                        //导航
                        methodBuilder.setNavigationElement(field.getNavigationElement());
                        //如果方法未定义则添加
                        if (!hasMethodDefined(methodBuilder, hasMethods)) {
                            list.add(methodBuilder);
                        }
                    }
                    //需要生成 getter 方法
                    if (flags.contains(InheritFlag.GETTER.name())) {
                        //是否需要生成自身的字段对应的方法
                        if (flags.contains(InheritFlag.OWN.name())) {
                            ownFlags.add(InheritFlag.GETTER.name());
                        }
                        //方法名
                        String prefix;
                        if (field.getType().equals(PsiType.BOOLEAN)) {
                            prefix = "is";
                        } else {
                            prefix = "get";
                        }
                        String methodName = prefix + fieldName.substring(0, 1).toUpperCase() +
                                fieldName.substring(1);
                        LightMethodBuilder methodBuilder =
                                new LightMethodBuilder(manager,
                                        JavaLanguage.INSTANCE,
                                        methodName);
                        //public
                        methodBuilder.setModifiers(PsiModifier.PUBLIC);
                        //返回值为字段类型
                        methodBuilder.setMethodReturnType(field.getType());
                        //所属的 Class
                        methodBuilder.setContainingClass(targetClass);
                        //导航
                        methodBuilder.setNavigationElement(field.getNavigationElement());
                        //如果方法未定义则添加
                        if (!hasMethodDefined(methodBuilder, hasMethods)) {
                            list.add(methodBuilder);
                        }
                    }
                    //需要生成 setter 方法
                    if (flags.contains(InheritFlag.SETTER.name())) {
                        //是否需要生成自身的字段对应的方法
                        if (flags.contains(InheritFlag.OWN.name())) {
                            ownFlags.add(InheritFlag.SETTER.name());
                        }
                        //方法名
                        String methodName = "set" + fieldName.substring(0, 1).toUpperCase() +
                                fieldName.substring(1);
                        LightMethodBuilder methodBuilder =
                                new LightMethodBuilder(manager,
                                        JavaLanguage.INSTANCE,
                                        methodName);
                        //public
                        methodBuilder.setModifiers(PsiModifier.PUBLIC);
                        //入参使用字段名和字段类型
                        methodBuilder.addParameter(field.getName(), field.getType());
                        //返回值为 void
                        methodBuilder.setMethodReturnType(PsiType.VOID);
                        //所属的 Class
                        methodBuilder.setContainingClass(targetClass);
                        //导航
                        methodBuilder.setNavigationElement(field.getNavigationElement());
                        //如果方法未定义则添加
                        if (!hasMethodDefined(methodBuilder, hasMethods)) {
                            list.add(methodBuilder);
                        }
                    }
                }
            };
            //执行方法 flags 回调处理
            getPsis(targetClass, PsiField.class, new HashSet<>(), new HashSet<>(),
                    fieldConsumer, null);

            //处理自身字段
            Collection<PsiField> ownFields = collectClassFieldsIntern(targetClass);
            for (PsiField ownField : ownFields) {
                fieldConsumer.accept(ownField, ownFlags);
            }
        }

        return (List<Psi>) list;
    }

    /**
     * 获得继承字段的注解
     */
    private Collection<PsiAnnotation> findFieldAnnotations(PsiClass targetClass) {
        Collection<PsiAnnotation> annotations = new ArrayList<>();
        for (PsiAnnotation annotation : targetClass.getAnnotations()) {
            if (FIELD_ANNOTATION_NAMES.contains(annotation.getQualifiedName())) {
                annotations.add(annotation);
            }
            if (FIELD_REPEATABLE_ANNOTATION_NAMES.contains(annotation.getQualifiedName())) {
                handleRepeatableAnnotation(annotation, annotations);
            }
        }
        return annotations;
    }

    /**
     * 获得继承方法的注解
     */
    private Collection<PsiAnnotation> findMethodAnnotations(PsiClass targetClass) {
        Collection<PsiAnnotation> annotations = new ArrayList<>();
        for (PsiAnnotation annotation : targetClass.getAnnotations()) {
            if (METHOD_ANNOTATION_NAMES.contains(annotation.getQualifiedName())) {
                annotations.add(annotation);
            }
            if (METHOD_REPEATABLE_ANNOTATION_NAMES.contains(annotation.getQualifiedName())) {
                handleRepeatableAnnotation(annotation, annotations);
            }
        }
        return annotations;
    }

    /**
     * 获得 Repeatable 注解
     */
    private void handleRepeatableAnnotation(PsiAnnotation annotation, Collection<PsiAnnotation> annotations) {
        PsiAnnotationMemberValue value = annotation.findAttributeValue("value");
        if (value != null) {
            PsiElement[] children = value.getChildren();
            for (PsiElement child : children) {
                if (child instanceof PsiAnnotation) {
                    annotations.add((PsiAnnotation) child);
                }
            }
        }
    }

    private Collection<PsiType> findTypes(PsiElement element) {
        Collection<PsiType> types = new HashSet<>();
        findTypes0(element, types);
        return types;
    }

    private void findTypes0(PsiElement element, Collection<PsiType> types) {
        if (element == null) {
            return;
        }
        if (element instanceof PsiTypeElement) {
            PsiType type = ((PsiTypeElement) element).getType();
            types.add(type);
        }
        for (PsiElement child : element.getChildren()) {
            findTypes0(child, types);
        }
    }

    private Boolean getBoolean(PsiElement element) {
        String string = getString(element);
        if (string == null) {
            return Boolean.FALSE;
        }
        return Boolean.valueOf(string);
    }

    private String getString(PsiElement element) {
        if (element instanceof PsiLiteralExpressionImpl) {
            return String.valueOf(((PsiLiteralExpressionImpl) element).getValue());
        }
        return null;
    }

    private Collection<String> findEnums(PsiElement element) {
        Collection<String> enums = new HashSet<>();
        findEnums0(element, enums);
        return enums;
    }

    private void findEnums0(PsiElement element, Collection<String> enums) {
        if (element == null) {
            return;
        }
        String anEnum = getEnum(element);
        if (anEnum != null) {
            enums.add(anEnum);
        }
        for (PsiElement child : element.getChildren()) {
            findEnums0(child, enums);
        }
    }

    private String getEnum(PsiElement element) {
        if (element instanceof PsiIdentifierImpl) {
            return ((PsiIdentifierImpl) element).getText();
        }
        return null;
    }

    private Collection<String> findStrings(PsiElement element) {
        Collection<String> strings = new HashSet<>();
        findStrings0(element, strings);
        return strings;
    }

    private void findStrings0(PsiElement element, Collection<String> strings) {
        if (element == null) {
            return;
        }
        String string = getString(element);
        if (string != null) {
            strings.add(string);
        }
        for (PsiElement child : element.getChildren()) {
            findStrings0(child, strings);
        }
    }

    /**
     * 字段是否定义
     */
    private boolean hasFieldDefined(PsiField field, Collection<PsiField> fields) {
        for (PsiField psiField : fields) {
            if (psiField.getName().equals(field.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 方法是否定义
     */
    private boolean hasMethodDefined(PsiMethod method, Collection<PsiMethod> methods) {
        for (PsiMethod psiMethod : methods) {
            if (psiMethod.getName().equals(method.getName()) &&
                    isParameterListEqual(psiMethod.getParameterList(), method.getParameterList())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 参数列表是否相等
     */
    private boolean isParameterListEqual(PsiParameterList list1, PsiParameterList list2) {
        PsiParameter[] parameters1 = list1.getParameters();
        PsiParameter[] parameters2 = list2.getParameters();
        if (parameters1.length != parameters2.length) {
            return false;
        }
        for (int i = 0; i < parameters1.length; i++) {
            PsiParameter parameter1 = parameters1[i];
            PsiParameter parameter2 = parameters2[i];
            PsiClass psiClass1 = PsiUtil.resolveClassInType(parameter1.getType());
            PsiClass psiClass2 = PsiUtil.resolveClassInType(parameter2.getType());
            if (psiClass1 != null && psiClass2 != null) {
                String qualifiedName1 = psiClass1.getQualifiedName();
                String qualifiedName2 = psiClass2.getQualifiedName();
                if (qualifiedName1 != null && qualifiedName2 != null) {
                    if (!qualifiedName1.equals(qualifiedName2)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 获得包含父类的所有字段，包含本功能生成的字段
     */
    public Collection<PsiField> getFields(PsiClass psiClass, boolean getSuper,
                                          Collection<String> hasHandleFieldClasses,
                                          Collection<String> hasHandleMethodClasses) {
        Collection<PsiField> fields = findFields(psiClass, hasHandleFieldClasses, hasHandleMethodClasses);
        if (getSuper) {
            PsiClass superClass = psiClass.getSuperClass();
            while (superClass != null) {
                fields.addAll(findFields(superClass, hasHandleFieldClasses, hasHandleMethodClasses));
                superClass = superClass.getSuperClass();
            }
        }
        return fields;
    }

    /**
     * 获得包含父类的所有方法，包含本功能生成的方法
     */
    public Collection<PsiMethod> getMethods(PsiClass psiClass, boolean getSuper,
                                            Collection<String> hasHandleFieldClasses,
                                            Collection<String> hasHandleMethodClasses) {
        Collection<PsiMethod> methods = findMethods(psiClass, hasHandleFieldClasses, hasHandleMethodClasses);
        if (getSuper) {
            PsiClass superClass = psiClass.getSuperClass();
            while (superClass != null) {
                methods.addAll(findMethods(superClass, hasHandleFieldClasses, hasHandleMethodClasses));
                superClass = superClass.getSuperClass();
            }
        }
        return methods;
    }

    /**
     * 获得所有字段，包含本功能生成的字段
     */
    private Collection<PsiField> findFields(PsiClass psiClass,
                                            Collection<String> hasHandleFieldClasses,
                                            Collection<String> hasHandleMethodClasses) {
        Collection<PsiField> fields = collectClassFieldsIntern(psiClass);
        List<PsiField> psis = getPsis(psiClass, PsiField.class,
                hasHandleFieldClasses, hasHandleMethodClasses,
                null, null);
        if (psis != null) {
            fields.addAll(psis);
        }
        return fields;
    }

    /**
     * 获得所有方法，包含本功能生成的方法
     */
    private Collection<PsiMethod> findMethods(PsiClass psiClass,
                                              Collection<String> hasHandleFieldClasses,
                                              Collection<String> hasHandleMethodClasses) {
        Collection<PsiMethod> methods = collectClassMethodsIntern(psiClass);
        List<PsiMethod> psis = getPsis(psiClass, PsiMethod.class,
                hasHandleFieldClasses, hasHandleMethodClasses,
                null, null);
        if (psis != null) {
            methods.addAll(psis);
        }
        return methods;
    }

    /**
     * 获得包含父类的所有字段，不包含本功能生成的字段
     */
    @Deprecated
    public static Collection<PsiField> getInternFieldsWithSuper(PsiClass psiClass) {
        if (psiClass == null) {
            return new ArrayList<>();
        }
        Collection<PsiField> fields = collectClassFieldsIntern(psiClass);
        PsiClass superClass = psiClass.getSuperClass();
        while (superClass != null) {
            fields.addAll(collectClassFieldsIntern(superClass));
            superClass = superClass.getSuperClass();
        }
        return fields;
    }

    /**
     * 获得包含父类的所有方法，不包含本功能生成的方法
     */
    @Deprecated
    public static Collection<PsiMethod> getInternMethodsWithSuper(PsiClass psiClass) {
        if (psiClass == null) {
            return new ArrayList<>();
        }
        Collection<PsiMethod> methods = collectClassMethodsIntern(psiClass);
        PsiClass superClass = psiClass.getSuperClass();
        while (superClass != null) {
            methods.addAll(collectClassMethodsIntern(superClass));
            superClass = superClass.getSuperClass();
        }
        return methods;
    }

    /**
     * 获得所有字段
     */
    public static Collection<PsiField> collectClassFieldsIntern(@NotNull PsiClass psiClass) {
        if (psiClass instanceof PsiExtensibleClass) {
            return new ArrayList<>(((PsiExtensibleClass) psiClass).getOwnFields());
        } else {
            return filterPsiElements(psiClass, PsiField.class);
        }
    }

    /**
     * 获得所有方法
     */
    public static Collection<PsiMethod> collectClassMethodsIntern(@NotNull PsiClass psiClass) {
        if (psiClass instanceof PsiExtensibleClass) {
            return new ArrayList<>(((PsiExtensibleClass) psiClass).getOwnMethods());
        } else {
            return filterPsiElements(psiClass, PsiMethod.class);
        }
    }

    private static <T extends PsiElement> Collection<T> filterPsiElements(@NotNull PsiClass psiClass, @NotNull Class<T> desiredClass) {
        return Arrays.stream(psiClass.getChildren()).filter(desiredClass::isInstance).map(desiredClass::cast).collect(Collectors.toList());
    }
}
