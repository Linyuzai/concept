package com.github.linyuzai.cloud.plugin.intellij;

import com.github.linyuzai.cloud.plugin.intellij.module.ModuleComponents;
import com.github.linyuzai.cloud.plugin.intellij.module.ModuleFileGenerator;
import com.github.linyuzai.cloud.plugin.intellij.module.ModuleModel;
import com.github.linyuzai.cloud.plugin.intellij.util.ConceptDialog;
import com.intellij.psi.*;
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
    public ConceptDialog createDialog(Context context) {
        PsiClass loginAnnotationClass = ConceptCloudUtils.searchInterface("Login", context.project);

        String className = ConceptCloudUtils.uppercaseFirst(context.selectPackage);
        PsiClass domainObjectClass = ConceptCloudUtils.searchDomainObjectClass(className,
                context.project, true);

        PsiClass[] classes = ModuleModel.getClassesInPackage(context.project, domainObjectClass);

        PsiClass domainCollectionClass = ModuleModel.getDomainCollectionClass(classes);

        PsiClass domainRepositoryClass = ModuleModel.getDomainRepositoryClass(classes);

        PsiClass domainServiceClass = ModuleModel.getClassByName(classes, className + "Service");

        context.withParentPackage();//to domain
        context.withParentPackage();//to module

        final ModuleModel model = new ModuleModel(context.userClassName,
                ModuleModel.getQualifiedName(loginAnnotationClass),
                context.selectModule, getModulePackage(context.psiPackage),
                ModuleModel.getQualifiedName(domainObjectClass),
                ModuleModel.getQualifiedName(domainCollectionClass),
                ModuleModel.getQualifiedName(domainRepositoryClass),
                ModuleModel.getQualifiedName(domainServiceClass),
                ModuleModel.getDomainDescriptionByComment(domainObjectClass));

        context.model = model;

        return ModuleComponents.createGenerateModuleCodeDialog(context.project, model, getDialogTitle());
    }

    private String getModulePackage(PsiPackage psiPackage) {
        if (psiPackage == null) {
            return "";
        }
        return psiPackage.getQualifiedName();
    }

    @Override
    public void onOk(Context context) {
        RecentsManager.getInstance(context.project).registerRecentEntry(
                RECENTS_KEY_USER_DOMAIN_CLASS, getModel(context).getUserClass().get());
        RecentsManager.getInstance(context.project).registerRecentEntry(
                RECENTS_KEY_LOGIN_ANNOTATION_CLASS, getModel(context).getLoginAnnotationClass().get());
    }

    @Override
    public void writeFile(File file, Context context) {
        ModuleFileGenerator.generate(getModel(context), file);
    }

    private ModuleModel getModel(Context context) {
        return (ModuleModel) context.model;
    }
}
