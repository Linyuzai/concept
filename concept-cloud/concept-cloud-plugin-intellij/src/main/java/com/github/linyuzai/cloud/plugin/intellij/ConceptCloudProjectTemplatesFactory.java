package com.github.linyuzai.cloud.plugin.intellij;

import com.intellij.icons.AllIcons;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.platform.ProjectTemplate;
import com.intellij.platform.ProjectTemplatesFactory;
import com.intellij.platform.templates.BuilderBasedTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@Deprecated
public class ConceptCloudProjectTemplatesFactory extends ProjectTemplatesFactory {

    @NotNull
    @Override
    public String[] getGroups() {
        return new String[]{"Concept Cloud"};
    }

    @NotNull
    @Override
    public ProjectTemplate[] createTemplates(@Nullable String group, @NotNull WizardContext context) {
        return new ProjectTemplate[]{};
        //return new ProjectTemplate[]{new BuilderBasedTemplate(new ConceptCloudModuleBuilder())};
        //return new ProjectTemplate[]{new BuilderBasedTemplate(new InternalGradleModuleBuilder())};
    }

    @Override
    public Icon getGroupIcon(String group) {
        return AllIcons.Nodes.Module;
    }
}
