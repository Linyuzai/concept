package com.github.linyuzai.builder.plugin.intellij;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;

public class ConceptBuilderLibraryUtils {

    private static final String CONCEPT_BUILDER_PACKAGE = "com.github.linyuzai.builder.core";

    public static boolean hasConceptBuilderLibrary(Project project) {
        ApplicationManager.getApplication().assertReadAccessAllowed();
        return CachedValuesManager.getManager(project).getCachedValue(project, () -> {
            PsiPackage aPackage = JavaPsiFacade.getInstance(project).findPackage(CONCEPT_BUILDER_PACKAGE);
            return new CachedValueProvider.Result<>(aPackage, ProjectRootManager.getInstance(project));
        }) != null;
    }
}
