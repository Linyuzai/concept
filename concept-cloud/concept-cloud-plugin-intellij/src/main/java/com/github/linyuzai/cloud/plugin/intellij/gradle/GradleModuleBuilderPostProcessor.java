package com.github.linyuzai.cloud.plugin.intellij.gradle;

import com.intellij.ide.util.newProjectWizard.AddModuleWizard;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.externalSystem.service.project.ProjectDataManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.plugins.gradle.service.project.open.GradleProjectImportUtil;

public class GradleModuleBuilderPostProcessor {

    public boolean postProcess(Module module) {
        // TODO: Find a way to use GradleModuleBuilder instead of GradleProjectImportBuilder when adding a child module to the parent
        Project project = module.getProject();
        //org.jetbrains.plugins.gradle.service.project.wizard.AbstractGradleModuleBuilder;
        /*VirtualFile gradleFile = findFileUnderRootInModule(module, "build.gradle");
        if (gradleFile == null) { // not a gradle project
            return true;
        } else {
            GradleProjectImportUtil.linkAndRefreshGradleProject();
            org.jetbrains.plugins.gradle.service.project.
            ProjectDataManager projectDataManager = ServiceManager.getService(ProjectDataManager.class);
            GradleProjectImportBuilder importBuilder = new GradleProjectImportBuilder(projectDataManager);
            GradleProjectImportProvider importProvider = new GradleProjectImportProvider(importBuilder);
            AddModuleWizard addModuleWizard =
                    new AddModuleWizard(project, gradleFile.getPath(), importProvider);
            if (addModuleWizard.getStepCount() > 0 && !addModuleWizard
                    .showAndGet()) { // user has cancelled import project prompt
                return true;
            } else { // user chose to import via the gradle import prompt
                importBuilder.commit(project, null, null);
                return false;
            }
        }*/
        return false;
    }
}
