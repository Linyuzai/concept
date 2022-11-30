package com.github.linyuzai.cloud.plugin.intellij;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ConceptCloudModuleBuilder extends ModuleBuilder {

    @Override
    public ModuleType<?> getModuleType() {
        return ConceptCloudModuleType.INSTANCE;
    }

    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull ModulesProvider modulesProvider) {
        return new ModuleWizardStep[]{getModuleWizardStep(wizardContext, modulesProvider)};
    }

    public ModuleWizardStep getModuleWizardStep(WizardContext context, ModulesProvider provider) {
        return new ModuleWizardStep() {
            @Override
            public JComponent getComponent() {
                return new JLabel("Concept Cloud");
            }

            @Override
            public void updateDataModel() {

            }
        };
    }
}
