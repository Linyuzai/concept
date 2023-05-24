package com.github.linyuzai.cloud.plugin.intellij;

import com.github.linyuzai.cloud.plugin.intellij.domain.DomainComponents;
import com.github.linyuzai.cloud.plugin.intellij.domain.DomainModel;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class GenerateDomainCodeAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            Messages.showMessageDialog("No project selected", "Error", null);
            return;
        }

        String projectName = project.getName();

        String moduleModuleName = projectName + "." + projectName + "-module";
        String domainModuleName = projectName + "." + projectName + "-domain";
        String userDomainModuleName = domainModuleName + ".domain-user";

        Collection<Module> modules = ModuleUtil.getModulesOfType(project, StdModuleTypes.JAVA);

        Module moduleModule = null;
        Module domainModule = null;
        Module userDomainModule = null;

        for (Module module : modules) {
            if (moduleModuleName.equals(module.getName())) {
                moduleModule = module;
            } else if (domainModuleName.equals(module.getName())) {
                domainModule = module;
            } else if (userDomainModuleName.equals(module.getName())) {
                userDomainModule = module;
            }
            if (moduleModule != null && domainModule != null && userDomainModule != null) {
                break;
            }
        }

        String userClassName = "";

        if (userDomainModule != null) {
            String suggestUserClassName = suggestUserClassName(userDomainModule);
            if (suggestUserClassName != null) {
                userClassName = suggestUserClassName;
            }
        }

        if (domainModule != null) {
            if (userClassName.isEmpty()) {
                String suggestUserClassName = suggestUserClassName(domainModule);
                if (suggestUserClassName != null) {
                    userClassName = suggestUserClassName;
                }
            }
        }

        final DomainModel model = DomainModel.create(userClassName, domainModule, "", "");

        /*val aClass = JavaPsiFacade.getInstance(project)
                .findClass(targetClassName, GlobalSearchScope.projectScope(project))*/

        DomainComponents.showGenerateDomainCodeDialog(project, model, () -> {
            Messages.showMessageDialog("Ok", "Ok", null);
            System.out.println(model);
            return null;
        });
    }

    private String suggestUserClassName(Module module) {
        Collection<VirtualFile> files = findFile(module, "User.java");
        for (VirtualFile file : files) {
            PsiFile psiFile = PsiManager.getInstance(module.getProject()).findFile(file);
            if (psiFile != null) {
                PsiClassOwner psiClassOwner = (PsiClassOwner) psiFile;
                PsiClass[] classes = psiClassOwner.getClasses();
                for (PsiClass psiClass : classes) {
                    if ("User".equals(psiClass.getName())) {
                        return psiClass.getQualifiedName();
                    }
                }
            }
        }
        return null;
    }

    private Collection<VirtualFile> findFile(Module module, String name) {
        return findFile(module, virtualFile -> name.equals(virtualFile.getName()));
    }

    private Collection<VirtualFile> findFile(Module module, Predicate<VirtualFile> predicate) {
        VirtualFile virtualFile = ProjectUtil.guessModuleDir(module);
        List<VirtualFile> files = new ArrayList<>();
        findFile0(virtualFile, predicate, files);
        return files;
    }

    private void findFile0(VirtualFile virtualFile, Predicate<VirtualFile> predicate, Collection<VirtualFile> files) {
        if (virtualFile == null) {
            return;
        }
        if (virtualFile.isDirectory()) {
            VirtualFile[] children = virtualFile.getChildren();
            for (VirtualFile child : children) {
                findFile0(child, predicate, files);
            }
        } else {
            if (predicate.test(virtualFile)) {
                files.add(virtualFile);
            }
        }
    }

    /*private void a() {

        //MoveMembersDialog
        JPanel panel = new JPanel(new BorderLayout());

        JPanel _panel;
        Box box = Box.createVerticalBox();

        _panel = new JPanel(new BorderLayout());
        JTextField sourceClassField = new JTextField();
        sourceClassField.setText(mySourceClassName);
        sourceClassField.setEditable(false);
        _panel.add(new JLabel(RefactoringBundle.message("move.members.move.members.from.label")), BorderLayout.NORTH);
        _panel.add(sourceClassField, BorderLayout.CENTER);
        box.add(_panel);

        box.add(Box.createVerticalStrut(10));

        _panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(RefactoringBundle.message("move.members.to.fully.qualified.name.label"));
        label.setLabelFor(myTfTargetClassName);
        _panel.add(label, BorderLayout.NORTH);
        _panel.add(myTfTargetClassName, BorderLayout.CENTER);
        _panel.add(myIntroduceEnumConstants, BorderLayout.SOUTH);
        box.add(_panel);

        myTfTargetClassName.getChildComponent().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent e) {
                myMemberInfoModel.updateTargetClass();
                validateButtons();
            }
        });

        panel.add(box, BorderLayout.CENTER);
        panel.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);

        validateButtons();
        return panel;
    }*/

    /*private ReferenceEditorComboWithBrowseButton createPackageChooser() {
        //MoveClassesOrPackagesDialog
        final ReferenceEditorComboWithBrowseButton packageChooser =
                new PackageNameReferenceEditorCombo("", myProject, RECENTS_KEY, RefactoringBundle.message("choose.destination.package"));
        final Document document = packageChooser.getChildComponent().getDocument();
        document.addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent e) {
                validateButtons();
            }
        });

        return packageChooser;
    }*/
}
