package com.github.linyuzai.cloud.plugin.intellij;

import com.intellij.codeInsight.actions.ReformatCodeProcessor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPackage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;

public abstract class GenerateCodeAction extends AnAction {

    public static final String RECENTS_KEY_USER_DOMAIN_CLASS = "ConceptCloud@UserDomainClass";

    public static class Context {

        Project project;

        String path;

        Module selectModule;

        PsiElement psiElement;

        PsiPackage psiPackage;

        String fullPackage;

        String selectPackage;

        String userClassName;

        Object model;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(ConceptCloudUtils.isSupport(e));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Context context = new Context();

        context.project = e.getProject();
        if (context.project == null) {
            Messages.showErrorDialog("No project selected", getDialogTitle());
            return;
        }

        VirtualFile virtualFile = e.getData(LangDataKeys.VIRTUAL_FILE);

        if (virtualFile == null) {
            Messages.showErrorDialog("No directory selected", getDialogTitle());
            return;
        }

        context.path = virtualFile.getCanonicalPath();
        if (context.path == null) {
            Messages.showErrorDialog("Can not get directory path", getDialogTitle());
            return;
        }

        context.selectModule = e.getData(LangDataKeys.MODULE);
        context.psiElement = e.getData(LangDataKeys.PSI_ELEMENT);

        context.psiPackage = ConceptCloudUtils.getPackage(context.psiElement);

        if (context.psiPackage == null) {
            context.fullPackage = "";
            context.selectPackage = "";
        } else {
            context.fullPackage = context.psiPackage.getQualifiedName();
            context.selectPackage = context.psiPackage.getName();
        }

        suggestModule(context);

        PsiClass userClass = ConceptCloudUtils.searchDomainObjectClass("User",
                context.project, false);

        context.userClassName = userClass == null ? "" : userClass.getQualifiedName();

        DialogBuilder dialog = createDialogBuilder(context);
        if (dialog.showAndGet()) {
            onOk(context);
            ProgressManager.getInstance().runProcessWithProgressSynchronously(() -> {
                try {
                    File dir = new File(context.path);

                    writeFile(dir, context);

                    VirtualFile vf = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(dir);
                    VfsUtil.markDirtyAndRefresh(false, true, false, vf);

                } catch (Throwable error) {
                    Messages.showErrorDialog(error.getMessage(), getDialogTitle());
                }
            }, getProgressTitle(), true, context.project);
        }
        LocalFileSystem.getInstance().refresh(false);
        new ReformatCodeProcessor(context.project, context.selectModule, false).run();
    }

    protected void suggestModule(Context context) {
        Collection<Module> modules = ModuleUtil.getModulesOfType(context.project, StdModuleTypes.JAVA);
        for (Module module : modules) {
            if (context.selectModule != null &&
                    context.selectModule.getName().endsWith(".main") &&
                    context.selectModule.getName().equals(module.getName() + ".main")) {
                context.selectModule = module;
                break;
            }
        }
    }

    public abstract DialogBuilder createDialogBuilder(Context context);

    public abstract void onOk(Context context);

    public abstract void writeFile(File file, Context context);

    public abstract String getDialogTitle();

    public abstract String getProgressTitle();
}
