package com.github.linyuzai.cloud.plugin.intellij;

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.platform.ProjectTemplate;
import com.intellij.platform.ProjectTemplatesFactory;
import com.intellij.platform.templates.BuilderBasedTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConceptCloudProjectTemplatesFactory extends ProjectTemplatesFactory {

    @NotNull
    @Override
    public String[] getGroups() {
        return new String[]{"Concept Cloud"};
    }

    @NotNull
    @Override
    public ProjectTemplate[] createTemplates(@Nullable String group, WizardContext context) {
        return new ProjectTemplate[]{new BuilderBasedTemplate(new ConceptCloudModuleBuilder())};
    }
}
