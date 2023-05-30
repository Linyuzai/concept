package com.github.linyuzai.cloud.plugin.intellij;

import com.github.linyuzai.cloud.plugin.intellij.module.ModuleComponents;
import com.github.linyuzai.cloud.plugin.intellij.module.ModuleFileGenerator;
import com.github.linyuzai.cloud.plugin.intellij.module.ModuleModel;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocToken;
import com.intellij.psi.util.PsiUtil;
import com.intellij.ui.RecentsManager;

import java.io.File;

public class GenerateModuleCodeAction extends GenerateCodeAction {

    @Override
    public String getDialogTitle() {
        return "Generate Module Code";
    }

    @Override
    public String getProgressTitle() {
        return "Generate module code...";
    }

    @Override
    public DialogBuilder createDialogBuilder(Context context) {
        String className = ConceptCloudUtils.uppercaseFirst(context.selectPackage);
        PsiClass domainObjectClass = ConceptCloudUtils.searchDomainObjectClass(className,
                context.project, true);

        PsiClass[] classes = getClassesInPackage(context, domainObjectClass);

        PsiClass domainCollectionClass = ConceptCloudUtils.getClassPredicateInterface(classes,
                psiInterface -> "com.github.linyuzai.domain.core.DomainCollection"
                        .equals(psiInterface.getQualifiedName()));

        PsiClass domainServiceClass = getClassByName(classes, className + "Service");

        PsiClass domainRepositoryClass = ConceptCloudUtils.getClassPredicateInterface(classes,
                psiInterface -> "com.github.linyuzai.domain.core.DomainRepository"
                        .equals(psiInterface.getQualifiedName()));

        String domainObjectClassName = domainObjectClass == null ? "" :
                domainObjectClass.getQualifiedName();

        String domainCollectionClassName = domainCollectionClass == null ? "" :
                domainCollectionClass.getQualifiedName();

        String domainServiceClassName = domainServiceClass == null ? "" :
                domainServiceClass.getQualifiedName();

        String domainRepositoryClassName = domainRepositoryClass == null ? "" :
                domainRepositoryClass.getQualifiedName();

        final ModuleModel model = new ModuleModel(context.userClassName,
                context.selectModule, context.fullPackage,
                domainObjectClassName == null ? "" : domainObjectClassName,
                domainCollectionClassName == null ? "" : domainCollectionClassName,
                domainServiceClassName == null ? "" : domainServiceClassName,
                domainRepositoryClassName == null ? "" : domainRepositoryClassName,
                getDomainDescriptionByComment(domainObjectClass));

        context.model = model;

        return ModuleComponents.createGenerateModuleCodeDialog(context.project, model, getDialogTitle());
    }

    private String getDomainDescriptionByComment(PsiClass psiClass) {
        if (psiClass == null) {
            return "";
        }
        PsiDocComment comment = psiClass.getDocComment();
        if (comment == null) {
            return "";
        }
        for (PsiElement child : comment.getChildren()) {
            if (child instanceof PsiDocToken) {
                if (((PsiDocToken) child).getTokenType() == JavaDocTokenType.DOC_COMMENT_DATA) {
                    return child.getText().trim();
                }
            }
        }
        return "";
    }

    private PsiClass getClassByName(PsiClass[] classes, String className) {
        for (PsiClass psiClass : classes) {
            if (className.equals(psiClass.getName())) {
                return psiClass;
            }
        }
        return null;
    }

    private PsiClass[] getClassesInPackage(Context context, PsiClass psiClass) {
        if (psiClass == null) {
            return new PsiClass[0];
        }
        String packageName = PsiUtil.getPackageName(psiClass);
        if (packageName == null) {
            return new PsiClass[0];
        }
        PsiPackage psiPackage = JavaPsiFacade.getInstance(context.project).findPackage(packageName);
        if (psiPackage == null) {
            return new PsiClass[0];
        }
        return psiPackage.getClasses();
    }

    @Override
    public void onOk(Context context) {
        RecentsManager.getInstance(context.project).registerRecentEntry(
                RECENTS_KEY_USER_DOMAIN_CLASS, getModel(context).getUserClass().get());
    }

    @Override
    public void writeFile(File file, Context context) {
        ModuleFileGenerator.generate(getModel(context), file);
    }

    private ModuleModel getModel(Context context) {
        return (ModuleModel) context.model;
    }
}
