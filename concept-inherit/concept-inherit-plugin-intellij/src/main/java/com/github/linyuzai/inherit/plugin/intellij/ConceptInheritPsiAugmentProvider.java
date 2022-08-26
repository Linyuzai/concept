package com.github.linyuzai.inherit.plugin.intellij;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.*;
import com.intellij.psi.augment.PsiAugmentProvider;
import com.intellij.psi.impl.light.LightFieldBuilder;
import com.intellij.psi.impl.light.LightMethodBuilder;
import com.intellij.psi.impl.light.LightModifierList;
import com.intellij.psi.impl.source.PsiExtensibleClass;
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ConceptInheritPsiAugmentProvider extends PsiAugmentProvider {

    private final Set<String> FIELD_ANNOTATION_NAMES = new HashSet<>(Arrays.asList(
            "com.github.linyuzai.inherit.core.annotation.InheritClass",
            "com.github.linyuzai.inherit.core.annotation.InheritField"));

    private final Set<String> FIELD_REPEATABLE_ANNOTATION_NAMES = new HashSet<>(Arrays.asList(
            "com.github.linyuzai.inherit.core.annotation.InheritClasses",
            "com.github.linyuzai.inherit.core.annotation.InheritFields"));

    private final Set<String> METHOD_ANNOTATION_NAMES = new HashSet<>(Arrays.asList(
            "com.github.linyuzai.inherit.core.annotation.InheritClass",
            "com.github.linyuzai.inherit.core.annotation.InheritMethod"));

    private final Set<String> METHOD_REPEATABLE_ANNOTATION_NAMES = new HashSet<>(Arrays.asList(
            "com.github.linyuzai.inherit.core.annotation.InheritClasses",
            "com.github.linyuzai.inherit.core.annotation.InheritMethods"));

    @Override
    protected @NotNull <Psi extends PsiElement> List<Psi> getAugments(@NotNull PsiElement element, @NotNull Class<Psi> type, @Nullable String nameHint) {
        List<Psi> psis = getPsis(element, type, new HashSet<>(), new HashSet<>());
        return psis == null ? super.getAugments(element, type, nameHint) : psis;
    }

    @SuppressWarnings("unchecked")
    private <Psi extends PsiElement> List<Psi> getPsis(PsiElement element, @NotNull Class<Psi> type,
                                                       Collection<String> hasHandleFieldClasses,
                                                       Collection<String> hasHandleMethodClasses) {
        System.out.println(ApplicationManager.getApplication().isUnitTestMode());
        if (!ConceptInheritLibraryUtils.hasConceptInheritLibrary(element.getProject())) {
            return null;
        }
        //处理 Class
        if (!(element instanceof PsiClass)) {
            return null;
        }

        List<PsiElement> list = new ArrayList<>();

        PsiClass targetClass = (PsiClass) element;
        if (type.isAssignableFrom(PsiField.class)) {
            if (hasHandleFieldClasses.contains(targetClass.getQualifiedName())) {
                return Collections.emptyList();
            }
            hasHandleFieldClasses.add(targetClass.getQualifiedName());

            Collection<PsiAnnotation> annotations = findFieldAnnotations(targetClass);

            for (PsiAnnotation annotation : annotations) {
                Collection<PsiType> sources = findTypes(annotation.findAttributeValue("sources"));
                Boolean inheritSuper = getBoolean(annotation.findAttributeValue("inheritSuper"));
                Collection<String> excludeFields = findStrings(annotation.findAttributeValue("excludeFields"));
                Collection<String> flags = findStrings(annotation.findAttributeValue("flags"));

                for (PsiType sourceType : sources) {
                    PsiClass sourceClass = PsiUtil.resolveClassInType(sourceType);
                    if (sourceClass == null) {
                        continue;
                    }

                    Collection<PsiField> fields =
                            getFields(sourceClass, inheritSuper, hasHandleFieldClasses, hasHandleMethodClasses);

                    Collection<PsiField> hasFields = collectClassFieldsIntern(targetClass);

                    for (PsiField field : fields) {
                        if (field.hasModifierProperty(PsiModifier.STATIC) ||
                                excludeFields.contains(field.getName()) ||
                                hasFieldDefined(field, hasFields)) {
                            continue;
                        }
                        list.add(field);
                    }
                }
            }
        }

        if (type.isAssignableFrom(PsiMethod.class)) {
            if (hasHandleMethodClasses.contains(targetClass.getQualifiedName())) {
                return Collections.emptyList();
            }
            hasHandleMethodClasses.add(targetClass.getQualifiedName());

            Collection<PsiAnnotation> annotations = findMethodAnnotations(targetClass);

            for (PsiAnnotation annotation : annotations) {
                Collection<PsiType> sources = findTypes(annotation.findAttributeValue("sources"));
                Boolean inheritSuper = getBoolean(annotation.findAttributeValue("inheritSuper"));
                Collection<String> excludeMethods = findStrings(annotation.findAttributeValue("excludeMethods"));
                Collection<String> flags = findStrings(annotation.findAttributeValue("flags"));

                for (PsiType sourceType : sources) {
                    PsiClass sourceClass = PsiUtil.resolveClassInType(sourceType);
                    if (sourceClass == null) {
                        continue;
                    }

                    Collection<PsiMethod> methods =
                            getMethods(sourceClass, inheritSuper, hasHandleFieldClasses, hasHandleMethodClasses);

                    Collection<PsiMethod> hasMethods = collectClassMethodsIntern(targetClass);

                    for (PsiMethod method : methods) {
                        if (method.hasModifierProperty(PsiModifier.STATIC) ||
                                excludeMethods.contains(method.getName()) ||
                                hasMethodDefined(method, hasMethods)) {
                            continue;
                        }
                        list.add(method);
                    }
                }
            }
        }

        return (List<Psi>) list;
    }

    private Collection<PsiAnnotation> findFieldAnnotations(PsiClass targetClass) {
        Collection<PsiAnnotation> annotations = new ArrayList<>();
        for (PsiAnnotation annotation : targetClass.getAnnotations()) {
            if (FIELD_ANNOTATION_NAMES.contains(annotation.getQualifiedName())) {
                annotations.add(annotation);
            }
            if (FIELD_REPEATABLE_ANNOTATION_NAMES.contains(annotation.getQualifiedName())) {
                handleRepeatableAnnotation(annotation, annotations);
            }
        }
        return annotations;
    }

    private Collection<PsiAnnotation> findMethodAnnotations(PsiClass targetClass) {
        Collection<PsiAnnotation> annotations = new ArrayList<>();
        for (PsiAnnotation annotation : targetClass.getAnnotations()) {
            if (METHOD_ANNOTATION_NAMES.contains(annotation.getQualifiedName())) {
                annotations.add(annotation);
            }
            if (METHOD_REPEATABLE_ANNOTATION_NAMES.contains(annotation.getQualifiedName())) {
                handleRepeatableAnnotation(annotation, annotations);
            }
        }
        return annotations;
    }

    private void handleRepeatableAnnotation(PsiAnnotation annotation, Collection<PsiAnnotation> annotations) {
        PsiAnnotationMemberValue value = annotation.findAttributeValue("value");
        if (value != null) {
            PsiElement[] children = value.getChildren();
            for (PsiElement child : children) {
                if (child instanceof PsiAnnotation) {
                    annotations.add((PsiAnnotation) child);
                }
            }
        }
    }

    private Collection<PsiType> findTypes(PsiElement element) {
        Collection<PsiType> types = new HashSet<>();
        findTypes0(element, types);
        return types;
    }

    private void findTypes0(PsiElement element, Collection<PsiType> types) {
        if (element == null) {
            return;
        }
        if (element instanceof PsiTypeElement) {
            PsiType type = ((PsiTypeElement) element).getType();
            types.add(type);
        }
        for (PsiElement child : element.getChildren()) {
            findTypes0(child, types);
        }
    }

    private Boolean getBoolean(PsiElement element) {
        String string = getString(element);
        if (string == null) {
            return Boolean.FALSE;
        }
        return Boolean.valueOf(string);
    }

    private String getString(PsiElement element) {
        if (element instanceof PsiLiteralExpressionImpl) {
            return String.valueOf(((PsiLiteralExpressionImpl) element).getValue());
        }
        return null;
    }

    private Collection<String> findStrings(PsiElement element) {
        Collection<String> strings = new HashSet<>();
        findStrings0(element, strings);
        return strings;
    }

    private void findStrings0(PsiElement element, Collection<String> strings) {
        if (element == null) {
            return;
        }
        String string = getString(element);
        if (string != null) {
            strings.add(string);
        }
        for (PsiElement child : element.getChildren()) {
            findStrings0(child, strings);
        }
    }

    private boolean hasFieldDefined(PsiField field, Collection<PsiField> fields) {
        for (PsiField psiField : fields) {
            if (psiField.getName().equals(field.getName())) {
                return true;
            }
        }
        return false;
    }

    private boolean hasMethodDefined(PsiMethod method, Collection<PsiMethod> methods) {
        for (PsiMethod psiMethod : methods) {
            if (psiMethod.getName().equals(method.getName()) &&
                    isParameterListEqual(psiMethod.getParameterList(), method.getParameterList())) {
                return true;
            }
        }
        return false;
    }

    private boolean isParameterListEqual(PsiParameterList list1, PsiParameterList list2) {
        PsiParameter[] parameters1 = list1.getParameters();
        PsiParameter[] parameters2 = list2.getParameters();
        if (parameters1.length != parameters2.length) {
            return false;
        }
        for (int i = 0; i < parameters1.length; i++) {
            PsiParameter parameter1 = parameters1[i];
            PsiParameter parameter2 = parameters2[i];
            PsiClass psiClass1 = PsiUtil.resolveClassInType(parameter1.getType());
            PsiClass psiClass2 = PsiUtil.resolveClassInType(parameter2.getType());
            if (psiClass1 != null && psiClass2 != null) {
                String qualifiedName1 = psiClass1.getQualifiedName();
                String qualifiedName2 = psiClass2.getQualifiedName();
                if (qualifiedName1 != null && qualifiedName2 != null) {
                    if (!qualifiedName1.equals(qualifiedName2)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Collection<PsiField> getFields(PsiClass psiClass, boolean getSuper,
                                          Collection<String> hasHandleFieldClasses,
                                          Collection<String> hasHandleMethodClasses) {
        Collection<PsiField> fields = findFields(psiClass, hasHandleFieldClasses, hasHandleMethodClasses);
        if (getSuper) {
            PsiClass superClass = psiClass.getSuperClass();
            while (superClass != null) {
                fields.addAll(findFields(superClass, hasHandleFieldClasses, hasHandleMethodClasses));
                superClass = superClass.getSuperClass();
            }
        }
        return fields;
    }

    public Collection<PsiMethod> getMethods(PsiClass psiClass, boolean getSuper,
                                            Collection<String> hasHandleFieldClasses,
                                            Collection<String> hasHandleMethodClasses) {
        Collection<PsiMethod> methods = findMethods(psiClass, hasHandleFieldClasses, hasHandleMethodClasses);
        if (getSuper) {
            PsiClass superClass = psiClass.getSuperClass();
            while (superClass != null) {
                methods.addAll(findMethods(superClass, hasHandleFieldClasses, hasHandleMethodClasses));
                superClass = superClass.getSuperClass();
            }
        }
        return methods;
    }

    private Collection<PsiField> findFields(PsiClass psiClass,
                                            Collection<String> hasHandleFieldClasses,
                                            Collection<String> hasHandleMethodClasses) {
        Collection<PsiField> fields = collectClassFieldsIntern(psiClass);
        List<PsiField> psis = getPsis(psiClass, PsiField.class, hasHandleFieldClasses, hasHandleMethodClasses);
        if (psis != null) {
            fields.addAll(psis);
        }
        return fields;
    }

    private Collection<PsiMethod> findMethods(PsiClass psiClass,
                                              Collection<String> hasHandleFieldClasses,
                                              Collection<String> hasHandleMethodClasses) {
        Collection<PsiMethod> methods = collectClassMethodsIntern(psiClass);
        List<PsiMethod> psis = getPsis(psiClass, PsiMethod.class, hasHandleFieldClasses, hasHandleMethodClasses);
        if (psis != null) {
            methods.addAll(psis);
        }
        return methods;
    }

    public static Collection<PsiField> collectClassFieldsIntern(@NotNull PsiClass psiClass) {
        if (psiClass instanceof PsiExtensibleClass) {
            return new ArrayList<>(((PsiExtensibleClass) psiClass).getOwnFields());
        } else {
            return filterPsiElements(psiClass, PsiField.class);
        }
    }

    public static Collection<PsiMethod> collectClassMethodsIntern(@NotNull PsiClass psiClass) {
        if (psiClass instanceof PsiExtensibleClass) {
            return new ArrayList<>(((PsiExtensibleClass) psiClass).getOwnMethods());
        } else {
            return filterPsiElements(psiClass, PsiMethod.class);
        }
    }

    private static <T extends PsiElement> Collection<T> filterPsiElements(@NotNull PsiClass psiClass, @NotNull Class<T> desiredClass) {
        return Arrays.stream(psiClass.getChildren()).filter(desiredClass::isInstance).map(desiredClass::cast).collect(Collectors.toList());
    }
}
