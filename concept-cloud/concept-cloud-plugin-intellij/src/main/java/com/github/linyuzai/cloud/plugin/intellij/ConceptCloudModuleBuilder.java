package com.github.linyuzai.cloud.plugin.intellij;

import com.intellij.ide.NewProjectWizardLegacy;
import com.intellij.ide.projectWizard.ProjectSettingsStep;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.GeneralModuleType;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.projectRoots.JavaSdkType;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.JdkComboBox;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.roots.ui.configuration.ProjectStructureConfigurable;
import com.intellij.openapi.roots.ui.configuration.SdkListItem;
import com.intellij.openapi.roots.ui.configuration.projectRoot.ProjectSdksModel;
import com.intellij.openapi.util.Condition;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.gradle.service.project.wizard.AbstractGradleModuleBuilder;
import org.jetbrains.plugins.gradle.service.project.wizard.GradleStructureWizardStep;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class ConceptCloudModuleBuilder extends AbstractGradleModuleBuilder {

    @Override
    public boolean isAvailable() {
        return NewProjectWizardLegacy.isAvailable();
    }

    @NotNull
    @Override
    public List<Class<? extends ModuleWizardStep>> getIgnoredSteps() {
        return Collections.singletonList(ProjectSettingsStep.class);
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Sentence) String getDescription() {
        return "Generate a project or module to support Spring Cloud and Spring Boot both simultaneous with Gradle";
    }

    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull ModulesProvider modulesProvider) {
        return new ModuleWizardStep[]{new GradleStructureWizardStep(this, wizardContext)};
    }

    public ModuleWizardStep getModuleWizardStep(WizardContext context, ModulesProvider provider) {
        return new ModuleWizardStep() {
            @Override
            public JComponent getComponent() {
                Project project = ProjectManager.getInstance().getDefaultProject();
                ProjectSdksModel model = ProjectStructureConfigurable.getInstance(project).getProjectJdksModel();
                return new JdkComboBox(project, model, new Condition<SdkTypeId>() {
                    @Override
                    public boolean value(SdkTypeId sdkTypeId) {
                        return sdkTypeId instanceof JavaSdkType && !((JavaSdkType) sdkTypeId).isDependent();
                    }
                }, new Condition<Sdk>() {
                    @Override
                    public boolean value(Sdk sdk) {
                        return true;
                    }
                }, new Condition<SdkListItem.SuggestedItem>() {

                    @Override
                    public boolean value(SdkListItem.SuggestedItem suggestedItem) {
                        return true;
                    }
                }, new Condition<SdkTypeId>() {
                    @Override
                    public boolean value(SdkTypeId sdkTypeId) {
                        return sdkTypeId instanceof JavaSdkType && !((JavaSdkType) sdkTypeId).isDependent();
                    }
                }, null);
            }

            @Override
            public void updateDataModel() {

            }
        };
    }
}
