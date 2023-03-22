/*
package com.github.linyuzai.cloud.plugin.intellij;

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
@Deprecated
public class ConceptCloudModuleType extends ModuleType<ConceptCloudModuleBuilder> {

    public static final ConceptCloudModuleType INSTANCE = new ConceptCloudModuleType();

    protected ConceptCloudModuleType() {
        super("concept.cloud");
    }

    @NotNull
    @Override
    public ConceptCloudModuleBuilder createModuleBuilder() {
        return new ConceptCloudModuleBuilder();
    }

    @Override
    public ModuleWizardStep @NotNull [] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull ConceptCloudModuleBuilder moduleBuilder, @NotNull ModulesProvider modulesProvider) {
        return new ModuleWizardStep[]{getModuleWizardStep(wizardContext, modulesProvider)};
    }

    public ModuleWizardStep getModuleWizardStep(WizardContext context, ModulesProvider provider) {
        return new ModuleWizardStep() {
            @Override
            public JComponent getComponent() {
                return new JLabel("Module Type");
            }

            @Override
            public void updateDataModel() {

            }
        };
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @NotNull
    @Override
    public String getName() {
        return "Concept Cloud Starter";
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getDescription() {
        return "Generate a project or module to support Spring Cloud and Spring Boot both";
    }

    @NotNull
    @Override
    public Icon getNodeIcon(boolean isOpened) {
        return AllIcons.Nodes.Module;
    }
}
*/
