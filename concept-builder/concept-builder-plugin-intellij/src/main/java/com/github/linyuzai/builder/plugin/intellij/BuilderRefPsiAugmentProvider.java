package com.github.linyuzai.builder.plugin.intellij;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import com.intellij.psi.impl.light.LightFieldBuilder;
import com.intellij.psi.impl.light.LightMethodBuilder;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BuilderRefPsiAugmentProvider extends PsiAugmentProvider {

    private static final Map<PsiElement, Map<Class<?>, List<PsiElement>>> cache = new ConcurrentHashMap<>();

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
        PsiClass psiClass = (PsiClass) element;
        //获得 Class 上的 @BuilderRef 注解
        PsiAnnotation annotation = psiClass.getAnnotation("com.github.linyuzai.builder.core.BuilderRef");
        if (annotation == null) {
            //注解不存在就清理缓存
            cache.getOrDefault(element, Collections.emptyMap()).remove(type);
            return null;
        }

        return (List<Psi>) cache.computeIfAbsent(element, e -> new ConcurrentHashMap<>())
                .computeIfAbsent(type, t -> {

                    //获得注解上的参数值
                    PsiAnnotationMemberValue value = annotation.findAttributeValue("value");

                    if (value == null) {
                        return null;
                    }

                    //获得注解上指定的 Class
                    PsiElement child = value.getFirstChild();

                    if (!(child instanceof PsiTypeElement)) {
                        return null;
                    }

                    // Type && Class
                    PsiType psiType = ((PsiTypeElement) child).getType();
                    PsiClass refClass = PsiUtil.resolveClassInType(psiType);

                    if (refClass == null) {
                        return null;
                    }

                    PsiManager manager = psiClass.getManager();
                    List<PsiElement> list = new ArrayList<>();

                    //如果需要方法
                    if (type.isAssignableFrom(PsiMethod.class)) {
                        //遍历属性
                        for (PsiField field : refClass.getAllFields()) {
                            LightMethodBuilder method =
                                    new LightMethodBuilder(manager,
                                            JavaLanguage.INSTANCE,
                                            field.getName());
                            //Public
                            method.addModifier(PsiModifier.PUBLIC);
                            //Class
                            method.setContainingClass(psiClass);
                            //导航
                            method.setNavigationElement(annotation);
                            //入参是字段名称和字段类型
                            method.addParameter(field.getName(), field.getType());
                            //返回值类型是 Builder
                            method.setMethodReturnType(PsiTypesUtil.getClassType(psiClass));
                            list.add(method);
                        }
                        //额外添加 build 方法
                        LightMethodBuilder build =
                                new LightMethodBuilder(manager,
                                        JavaLanguage.INSTANCE,
                                        "build");
                        //Public
                        build.addModifier(PsiModifier.PUBLIC);
                        //Class
                        build.setContainingClass(psiClass);
                        //导航
                        build.setNavigationElement(annotation);
                        //返回值是 @BuilderRef 指定的 Class
                        build.setMethodReturnType(psiType);
                        list.add(build);
                    }

                    //如果需要属性
                    if (type.isAssignableFrom(PsiField.class)) {
                        for (PsiField field : refClass.getAllFields()) {
                            LightFieldBuilder lfb =
                                    new LightFieldBuilder(manager,
                                            field.getName(),
                                            field.getType());
                            //Protected 用于继承
                            lfb.setModifiers(PsiModifier.PROTECTED);
                            //Class
                            lfb.setContainingClass(psiClass);
                            //导航
                            lfb.setNavigationElement(annotation);
                            list.add(lfb);
                        }
                    }
                    return list;
                });
    }
}
