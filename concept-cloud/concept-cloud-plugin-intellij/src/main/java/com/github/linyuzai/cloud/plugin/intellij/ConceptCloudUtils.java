package com.github.linyuzai.cloud.plugin.intellij;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;

import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.linyuzai.cloud.plugin.intellij.builder.JavaBuilderKt.TYPE_DOMAIN_ENTITY;
import static com.github.linyuzai.cloud.plugin.intellij.builder.JavaBuilderKt.TYPE_DOMAIN_VALUE;

public class ConceptCloudUtils {

    public static boolean isSupport(AnActionEvent e) {
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

    public static String uppercaseFirst(String str) {
        if (str == null) {
            return null;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static PsiPackage getPackage(PsiElement psiElement) {
        if (psiElement instanceof PsiDirectory) {
            return JavaDirectoryService.getInstance().getPackageInSources((PsiDirectory) psiElement);
        }
        return null;
    }

    public static PsiClass getClassPredicateInterface(PsiClass[] classes, Predicate<PsiClass> predicate) {
        for (PsiClass psiClass : classes) {
            PsiClass[] interfaces = psiClass.getInterfaces();
            for (PsiClass anInterface : interfaces) {
                if (predicate.test(anInterface)) {
                    return psiClass;
                }
            }
            /*PsiClass aClass = getClassPredicateInterface(interfaces, predicate);
            if (aClass != null) {
                return aClass;
            }*/
        }
        return null;
    }

    public static PsiClass getDomainObjectClass(PsiClass[] classes) {
        return getClassPredicateInterface(classes, psiInterface ->
                TYPE_DOMAIN_ENTITY.equals(psiInterface.getQualifiedName()) ||
                        TYPE_DOMAIN_VALUE.equals(psiInterface.getQualifiedName()));
    }

    public static PsiClass searchClass(String name, Project project, boolean forceDomain,
                                       Function<PsiClass[], PsiClass> domainFilter) {
        if (name == null) {
            return null;
        }
        final PsiShortNamesCache cache = PsiShortNamesCache.getInstance(project);
        PsiClass[] allClasses = cache.getClassesByName(name, GlobalSearchScope.allScope(project));
        PsiClass psiClass = domainFilter.apply(allClasses);
        if (psiClass == null) {
            if (forceDomain) {
                return null;
            }
            PsiClass[] classes = cache.getClassesByName(name, GlobalSearchScope.projectScope(project));
            if (classes.length == 1) {
                return classes[0];
            } else {
                return null;
            }
        } else {
            return psiClass;
        }
    }

    public static PsiClass searchDomainObjectClass(String name, Project project, boolean forceDomain) {
        return searchClass(name, project, forceDomain, ConceptCloudUtils::getDomainObjectClass);
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

    /*val aClass = JavaPsiFacade.getInstance(project)
                .findClass(targetClassName, GlobalSearchScope.projectScope(project))*/

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
