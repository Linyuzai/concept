package com.github.linyuzai.cloud.plugin.intellij;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;

public class ConceptCloudSupport {

    public static boolean isEnabled(AnActionEvent e) {
        Project project = e.getProject();

        if (project == null) {
            return false;
        }
        Module module = e.getData(LangDataKeys.MODULE);
        if (module == null) {
            return false;
        }
        /*Object[] objects = e.getData(LangDataKeys.SELECTED_ITEMS);
        if (objects == null || objects.length != 1) {
            return false;
        }*/
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        return psiElement instanceof PsiDirectory;
    }
}
