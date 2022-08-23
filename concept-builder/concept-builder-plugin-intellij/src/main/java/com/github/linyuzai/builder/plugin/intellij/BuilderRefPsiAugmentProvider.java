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
        if (!(element instanceof PsiClass)) {
            return null;
        }
        PsiClass psiClass = (PsiClass) element;
        PsiAnnotation annotation = psiClass.getAnnotation("com.github.linyuzai.builder.core.BuilderRef");
        if (annotation == null) {
            cache.getOrDefault(element, Collections.emptyMap()).remove(type);
            return null;
        }

        return (List<Psi>) cache.computeIfAbsent(element, e -> new ConcurrentHashMap<>())
                .computeIfAbsent(type, t -> {

                    PsiAnnotationMemberValue value = annotation.findAttributeValue("value");

                    if (value == null) {
                        return null;
                    }

                    PsiElement child = value.getFirstChild();

                    if (!(child instanceof PsiTypeElement)) {
                        return null;
                    }

                    PsiType psiType = ((PsiTypeElement) child).getType();
                    PsiClass refClass = PsiUtil.resolveClassInType(psiType);

                    if (refClass == null) {
                        return null;
                    }

                    PsiManager manager = psiClass.getManager();
                    List<PsiElement> list = new ArrayList<>();
                    if (type.isAssignableFrom(PsiMethod.class)) {
                        for (PsiField field : refClass.getAllFields()) {
                            LightMethodBuilder method =
                                    new LightMethodBuilder(manager,
                                            JavaLanguage.INSTANCE,
                                            field.getName());
                            method.addModifier(PsiModifier.PUBLIC);
                            method.setContainingClass(psiClass);
                            method.setNavigationElement(annotation);
                            method.addParameter(field.getName(), field.getType());
                            method.setMethodReturnType(PsiTypesUtil.getClassType(psiClass));
                            list.add(method);
                        }
                        LightMethodBuilder build =
                                new LightMethodBuilder(manager,
                                        JavaLanguage.INSTANCE,
                                        "build");
                        build.addModifier(PsiModifier.PUBLIC);
                        build.setContainingClass(psiClass);
                        build.setNavigationElement(annotation);
                        build.setMethodReturnType(psiType);
                        list.add(build);
                    } else if (type.isAssignableFrom(PsiField.class)) {
                        for (PsiField field : refClass.getAllFields()) {
                            LightFieldBuilder lfb =
                                    new LightFieldBuilder(manager,
                                            field.getName(),
                                            field.getType());
                            lfb.setModifiers(PsiModifier.PROTECTED);
                            lfb.setContainingClass(psiClass);
                            lfb.setNavigationElement(annotation);
                            list.add(lfb);
                        }
                    }
                    return list;
                });
    }
}
