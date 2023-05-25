package com.github.linyuzai.cloud.plugin.intellij;

import com.github.linyuzai.cloud.plugin.intellij.domain.DomainComponents;
import com.github.linyuzai.cloud.plugin.intellij.domain.DomainModel;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.ui.RecentsManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class GenerateDomainCodeAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(ConceptCloudSupport.isEnabled(e));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            Messages.showMessageDialog("No project selected", "Error", null);
            return;
        }

        VirtualFile virtualFile = e.getData(LangDataKeys.VIRTUAL_FILE);

        if (virtualFile == null) {
            Messages.showMessageDialog("No directory selected", "Error", null);
            return;
        }

        Module selectModule = e.getData(LangDataKeys.MODULE);
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);

        String domainPackage = "";
        String domainClassName = "";

        if (psiElement instanceof PsiDirectory) {
            PsiPackage psiPackage = JavaDirectoryService.getInstance().getPackageInSources((PsiDirectory) psiElement);
            if (psiPackage != null) {
                domainPackage = psiPackage.getQualifiedName();
                String packageName = psiPackage.getName();
                if (packageName != null) {
                    domainClassName = packageName.substring(0, 1).toUpperCase() +
                            packageName.substring(1);
                }
            }
        }

        String projectName = project.getName();

        String domainModuleName = projectName + "." + projectName + "-domain";
        String userDomainModuleName = domainModuleName + ".domain-user";

        Collection<Module> modules = ModuleUtil.getModulesOfType(project, StdModuleTypes.JAVA);

        Module domainModule = null;
        Module userDomainModule = null;

        for (Module module : modules) {
            if (selectModule != null &&
                    selectModule.getName().endsWith(".main") &&
                    selectModule.getName().equals(module.getName() + ".main")) {
                selectModule = module;
            } else if (domainModuleName.equals(module.getName())) {
                domainModule = module;
            } else if (userDomainModuleName.equals(module.getName())) {
                userDomainModule = module;
            }
            if (domainModule != null && userDomainModule != null) {
                break;
            }
        }

        String userClassName = suggestUserClassName(project);

        final DomainModel model = new DomainModel(userClassName == null ? "" : userClassName,
                selectModule, domainPackage, domainClassName);

        /*val aClass = JavaPsiFacade.getInstance(project)
                .findClass(targetClassName, GlobalSearchScope.projectScope(project))*/

        DomainComponents.showGenerateDomainCodeDialog(project, model, () -> {
            RecentsManager recentsManager = RecentsManager.getInstance(project);
            recentsManager.registerRecentEntry(
                    DomainModel.getRECENTS_KEY_USER_DOMAIN_CLASS(),
                    model.getUserClassProperty().get());
            recentsManager.registerRecentEntry(
                    DomainModel.getRECENTS_KEY_DOMAIN_PACKAGE(),
                    model.getDomainPackageProperty().get());

            String path = virtualFile.getCanonicalPath();

            LocalFileSystem.getInstance().refresh(true);
            Messages.showMessageDialog("Ok", "Ok", null);
            return null;
        });
    }

    private PsiClass getUserDomainClass(PsiClass[] classes) {
        for (PsiClass psiClass : classes) {
            PsiClass[] interfaces = psiClass.getInterfaces();
            if (interfaces.length == 0) {
                continue;
            }
            for (PsiClass anInterface : interfaces) {
                if ("com.github.linyuzai.domain.core.DomainEntity"
                        .equals(anInterface.getQualifiedName())) {
                    return psiClass;
                }
            }
            PsiClass aClass = getUserDomainClass(interfaces);
            if (aClass != null) {
                return aClass;
            }
        }
        return null;
    }

    private String suggestUserClassName(Project project) {
        final PsiShortNamesCache cache = PsiShortNamesCache.getInstance(project);
        PsiClass[] allClasses = cache.getClassesByName("User", GlobalSearchScope.allScope(project));
        PsiClass userDomainClass = getUserDomainClass(allClasses);
        if (userDomainClass == null) {
            PsiClass[] classes = cache.getClassesByName("User", GlobalSearchScope.projectScope(project));
            if (classes.length == 1) {
                return classes[0].getQualifiedName();
            } else {
                return null;
            }
        } else {
            return userDomainClass.getQualifiedName();
        }
        /*Collection<VirtualFile> files = findFile(module, "User.java");
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
        return null;*/
    }

    /*private Collection<VirtualFile> findFile(Module module, String name) {
        return findFile(module, virtualFile -> name.equals(virtualFile.getName()));
    }*/

    /*private Collection<VirtualFile> findFile(Module module, Predicate<VirtualFile> predicate) {
        VirtualFile virtualFile = ProjectUtil.guessModuleDir(module);
        List<VirtualFile> files = new ArrayList<>();
        findFile0(virtualFile, predicate, files);
        return files;
    }*/

    /*private void findFile0(VirtualFile virtualFile, Predicate<VirtualFile> predicate, Collection<VirtualFile> files) {
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
    }*/

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
