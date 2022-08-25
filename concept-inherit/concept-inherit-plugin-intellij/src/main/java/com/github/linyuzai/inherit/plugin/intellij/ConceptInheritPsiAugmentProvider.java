package com.github.linyuzai.inherit.plugin.intellij;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import com.intellij.psi.impl.light.LightFieldBuilder;
import com.intellij.psi.impl.light.LightMethodBuilder;
import com.intellij.psi.impl.light.LightModifierList;
import com.intellij.psi.impl.source.PsiExtensibleClass;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ConceptInheritPsiAugmentProvider extends PsiAugmentProvider {

    private final Set<String> ANNOTATION_NAMES = new HashSet<>(Arrays.asList(
            "com.github.linyuzai.inherit.core.annotation.InheritClass",
            "com.github.linyuzai.inherit.core.annotation.InheritField",
            "com.github.linyuzai.inherit.core.annotation.InheritMethod"));

    private final Set<String> REPEATABLE_ANNOTATION_NAMES = new HashSet<>(Arrays.asList(
            "com.github.linyuzai.inherit.core.annotation.InheritClasses",
            "com.github.linyuzai.inherit.core.annotation.InheritFields",
            "com.github.linyuzai.inherit.core.annotation.InheritMethods"));

    @Override
    protected @NotNull <Psi extends PsiElement> List<Psi> getAugments(@NotNull PsiElement element, @NotNull Class<Psi> type, @Nullable String nameHint) {
        List<Psi> psis = getPsis(element, type);
        return psis == null ? super.getAugments(element, type, nameHint) : psis;
    }

    @SuppressWarnings("unchecked")
    private <Psi extends PsiElement> List<Psi> getPsis(PsiElement element, @NotNull Class<Psi> type) {
        if (!ConceptInheritLibraryUtils.hasConceptInheritLibrary(element.getProject())) {
            return null;
        }
        //处理 Class
        if (!(element instanceof PsiClass)) {
            return null;
        }
        PsiClass targetClass = (PsiClass) element;
        PsiManager manager = targetClass.getManager();

        Collection<PsiAnnotation> annotations = findAnnotations(targetClass);

        List<PsiElement> list = new ArrayList<>();

        for (PsiAnnotation annotation : annotations) {
            Collection<PsiType> sources = findTypes(annotation.findAttributeValue("sources"));
            Boolean inheritSuper = getBoolean(annotation.findAttributeValue("inheritSuper"));
            Collection<String> excludeFields = findStrings(annotation.findAttributeValue("excludeFields"));
            Collection<String> excludeMethods = findStrings(annotation.findAttributeValue("excludeMethods"));
            Collection<String> flags = findStrings(annotation.findAttributeValue("flags"));

            for (PsiType sourceType : sources) {
                PsiClass sourceClass = PsiUtil.resolveClassInType(sourceType);
                if (sourceClass == null) {
                    continue;
                }

                //如果需要属性
                if (type.isAssignableFrom(PsiField.class)) {
                    for (PsiField field : getFields(sourceClass, targetClass, inheritSuper)) {
                        if (field.hasModifierProperty(PsiModifier.STATIC) ||
                                excludeFields.contains(field.getName())) {
                            continue;
                        }
                        list.add(field);
                    }
                }

                //如果需要方法
                if (type.isAssignableFrom(PsiMethod.class)) {
                    for (PsiMethod method : getMethods(sourceClass, targetClass, inheritSuper)) {
                        if (method.hasModifierProperty(PsiModifier.STATIC) ||
                                excludeMethods.contains(method.getName())) {
                            continue;
                        }
                        list.add(method);
                    }
                }
            }
        }

        /*//获得注解上指定的 Class
        PsiElement refElement = value.getFirstChild();

        if (!(refElement instanceof PsiTypeElement)) {

            return null;
        }

        // Type && Class
        PsiType refType = ((PsiTypeElement) refElement).getType();
        PsiClass refClass = PsiUtil.resolveClassInType(refType);

        if (refClass == null) {
            return null;
        }

        PsiManager manager = targetClass.getManager();


        //如果需要方法
        if (type.isAssignableFrom(PsiMethod.class)) {
            //遍历属性
            for (PsiField field : refClass.getAllFields()) {
                if (field.hasModifierProperty(PsiModifier.STATIC)) {
                    continue;
                }
                LightMethodBuilder method =
                        new LightMethodBuilder(manager,
                                JavaLanguage.INSTANCE,
                                field.getName());
                //Public
                method.addModifier(PsiModifier.PUBLIC);
                //Class
                method.setContainingClass(targetClass);
                //导航
                method.setNavigationElement(annotation);
                //入参是字段名称和字段类型
                method.addParameter(field.getName(), field.getType());
                //返回值类型是 Builder
                method.setMethodReturnType(PsiTypesUtil.getClassType(targetClass));
                list.add(method);
            }

            if (!hasBuildMethod(targetClass)) {
                //额外添加 build 方法
                LightMethodBuilder build =
                        new LightMethodBuilder(manager,
                                JavaLanguage.INSTANCE,
                                "build");
                //Public
                build.addModifier(PsiModifier.PUBLIC);
                //Class
                build.setContainingClass(targetClass);
                //导航
                build.setNavigationElement(annotation);
                //返回值是 @BuilderRef 指定的 Class
                build.setMethodReturnType(refType);
                list.add(build);
            }
        }

        //如果需要属性
        if (type.isAssignableFrom(PsiField.class)) {
            for (PsiField field : refClass.getAllFields()) {
                if (field.hasModifierProperty(PsiModifier.STATIC)) {
                    continue;
                }
                LightFieldBuilder lfb =
                        new LightFieldBuilder(manager,
                                field.getName(),
                                field.getType());
                //Protected 用于继承
                lfb.setModifiers(PsiModifier.PROTECTED);
                //Class
                lfb.setContainingClass(targetClass);
                //导航
                lfb.setNavigationElement(annotation);
                list.add(lfb);
            }
        }
*/
        return (List<Psi>) list;
    }

    private Collection<PsiAnnotation> findAnnotations(PsiClass targetClass) {
        Collection<PsiAnnotation> annotations = new ArrayList<>();
        for (PsiAnnotation annotation : targetClass.getAnnotations()) {
            if (ANNOTATION_NAMES.contains(annotation.getQualifiedName())) {
                annotations.add(annotation);
            }
            if (REPEATABLE_ANNOTATION_NAMES.contains(annotation.getQualifiedName())) {
                handleRepeatableAnnotation(annotation, annotations);
            }
        }

        return annotations;
    }

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

    private boolean hasBuildMethod(PsiClass builderClass) {
        Collection<PsiMethod> methods = collectClassMethodsIntern(builderClass);
        for (PsiMethod method : methods) {
            if ("build".equals(method.getName()) && method.getParameterList().isEmpty()) {
                return true;
            }
        }
        PsiClass superClass = builderClass.getSuperClass();
        if (superClass == null) {
            return false;
        }
        PsiMethod[] builds = superClass.findMethodsByName("build", true);
        for (PsiMethod build : builds) {
            if (build.getParameterList().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public Collection<PsiField> getFields(PsiClass psiClass, PsiClass targetClass, boolean getSuper) {
        Collection<PsiField> fields = new HashSet<>(collectClassFieldsIntern(psiClass));
        if (psiClass != targetClass) {
            List<PsiField> psis = getPsis(psiClass, PsiField.class);
            if (psis != null) {
                fields.addAll(psis);
            }
        }
        if (getSuper) {
            PsiClass superClass = psiClass.getSuperClass();
            while (superClass != null) {
                fields.addAll(collectClassFieldsIntern(superClass));
                List<PsiField> psis = getPsis(superClass, PsiField.class);
                if (psis != null) {
                    fields.addAll(psis);
                }
                superClass = superClass.getSuperClass();
            }
        }
        return fields;
    }

    public Collection<PsiMethod> getMethods(PsiClass psiClass, PsiClass targetClass, boolean getSuper) {
        Collection<PsiMethod> methods = new HashSet<>(collectClassMethodsIntern(psiClass));
        if (psiClass != targetClass) {
            List<PsiMethod> psis = getPsis(psiClass, PsiMethod.class);
            if (psis != null) {
                methods.addAll(psis);
            }
        }
        if (getSuper) {
            PsiClass superClass = psiClass.getSuperClass();
            while (superClass != null) {
                methods.addAll(collectClassMethodsIntern(superClass));
                List<PsiMethod> psis = getPsis(superClass, PsiMethod.class);
                if (psis != null) {
                    methods.addAll(psis);
                }
                superClass = superClass.getSuperClass();
            }
        }
        return methods;
    }

    public static Collection<PsiField> collectClassFieldsIntern(@NotNull PsiClass psiClass) {
        if (psiClass instanceof PsiExtensibleClass) {
            return new ArrayList<>(((PsiExtensibleClass) psiClass).getOwnFields());
        } else {
            return filterPsiElements(psiClass, PsiField.class);
        }
    }

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
