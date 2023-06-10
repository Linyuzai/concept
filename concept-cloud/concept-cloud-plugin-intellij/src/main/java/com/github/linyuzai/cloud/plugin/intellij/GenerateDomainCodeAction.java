package com.github.linyuzai.cloud.plugin.intellij;

import com.github.linyuzai.cloud.plugin.intellij.domain.DomainComponents;
import com.github.linyuzai.cloud.plugin.intellij.domain.DomainFileGenerator;
import com.github.linyuzai.cloud.plugin.intellij.domain.DomainModel;
import com.github.linyuzai.cloud.plugin.intellij.util.ConceptDialog;
import com.intellij.ui.RecentsManager;

import java.io.File;

public class GenerateDomainCodeAction extends GenerateCodeAction {

    @Override
    public String getDialogTitle() {
        return "Generate Domain Code";
    }

    @Override
    public String getProgressTitle() {
        return "Generate domain code...";
    }

    @Override
    public ConceptDialog createDialog(Context context) {
        final DomainModel model = new DomainModel(context.userClassName,
                context.selectModule, context.fullPackage,
                ConceptCloudUtils.uppercaseFirst(context.selectPackage));

        context.model = model;

        return DomainComponents.createGenerateDomainCodeDialog(context.project, model, getDialogTitle());
    }

    @Override
    public void onOk(Context context) {
        DomainModel model = getModel(context);
        model.validProps();
        RecentsManager.getInstance(context.project).registerRecentEntry(
                RECENTS_KEY_USER_DOMAIN_CLASS, model.getUserClass().get());
    }

    @Override
    public void writeFile(File file, Context context) {
        DomainFileGenerator.generate(getModel(context), file);
    }

    private DomainModel getModel(Context context) {
        return (DomainModel) context.model;
    }
}
