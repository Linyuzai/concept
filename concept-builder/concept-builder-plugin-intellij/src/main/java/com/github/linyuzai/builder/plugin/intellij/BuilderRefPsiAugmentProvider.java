package com.github.linyuzai.builder.plugin.intellij;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.lang.jvm.JvmMethod;
import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import com.intellij.psi.impl.light.LightFieldBuilder;
import com.intellij.psi.impl.light.LightMethodBuilder;
import com.intellij.psi.impl.source.PsiExtensibleClass;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BuilderRefPsiAugmentProvider extends PsiAugmentProvider {

    @Override
    protected @NotNull <Psi extends PsiElement> List<Psi> getAugments(@NotNull PsiElement element, @NotNull Class<Psi> type, @Nullable String nameHint) {
        List<Psi> psis = getPsis(element, type);
        return psis == null ? super.getAugments(element, type, nameHint) : psis;
    }

    @SuppressWarnings("unchecked")
    private <Psi extends PsiElement> List<Psi> getPsis(PsiElement element, @NotNull Class<Psi> type) {
        //处理 Class
        if (!(element instanceof PsiClass)) {
            return null;
        }
        PsiClass builderClass = (PsiClass) element;
        //获得 Class 上的 @BuilderRef 注解
        PsiAnnotation annotation = builderClass.getAnnotation("com.github.linyuzai.builder.core.BuilderRef");
        if (annotation == null) {
            return null;
        }

        //获得注解上的参数值
        PsiAnnotationMemberValue value = annotation.findAttributeValue("value");

        if (value == null) {
            return null;
        }

        //获得注解上指定的 Class
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

        PsiManager manager = builderClass.getManager();
        List<PsiElement> list = new ArrayList<>();

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
                method.setContainingClass(builderClass);
                //导航
                method.setNavigationElement(annotation);
                //入参是字段名称和字段类型
                method.addParameter(field.getName(), field.getType());
                //返回值类型是 Builder
                method.setMethodReturnType(PsiTypesUtil.getClassType(builderClass));
                list.add(method);
            }

            if (!hasBuildMethod(builderClass)) {
                //额外添加 build 方法
                LightMethodBuilder build =
                        new LightMethodBuilder(manager,
                                JavaLanguage.INSTANCE,
                                "build");
                //Public
                build.addModifier(PsiModifier.PUBLIC);
                //Class
                build.setContainingClass(builderClass);
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
                lfb.setContainingClass(builderClass);
                //导航
                lfb.setNavigationElement(annotation);
                list.add(lfb);
            }
        }

        return (List<Psi>) list;
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
