package com.github.linyuzai.cloud.plugin.intellij.module

import com.github.linyuzai.cloud.plugin.intellij.ConceptCloudUtils
import com.github.linyuzai.cloud.plugin.intellij.ConceptGraphProperty
import com.github.linyuzai.cloud.plugin.intellij.ConceptGraphPropertyImpl
import com.github.linyuzai.cloud.plugin.intellij.ConceptPropertyGraph
import com.github.linyuzai.cloud.plugin.intellij.builder.TYPE_DOMAIN_COLLECTION
import com.github.linyuzai.cloud.plugin.intellij.builder.TYPE_DOMAIN_REPOSITORY
import com.intellij.openapi.module.Module
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.observable.properties.GraphPropertyImpl
import com.intellij.openapi.observable.properties.PropertyGraph
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaDocTokenType
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.javadoc.PsiDocToken
import com.intellij.psi.util.PsiUtil

data class ModuleModel(
    val initUserClass: String,
    val initLoginAnnotationClass: String,
    val initModuleModule: Module?,
    val initModulePackage: String,
    val initDomainObjectClass: String,
    val initDomainCollectionClass: String,
    val initDomainRepositoryClass: String,
    val initDomainServiceClass: String,
    val initDomainDescription: String
) {
    val propertyGraph: ConceptPropertyGraph = ConceptPropertyGraph()
    val userClass: ConceptGraphProperty<String> = property { initUserClass }
    val loginAnnotationClass: ConceptGraphProperty<String> = property { initLoginAnnotationClass }
    val moduleModule: ConceptGraphProperty<Module?> = property { initModuleModule }
    val modulePackage: ConceptGraphProperty<String> = property { initModulePackage }
    val domainObjectClass: ConceptGraphProperty<String> = property { initDomainObjectClass }
    val domainCollectionClass: ConceptGraphProperty<String> = property { initDomainCollectionClass }
    val domainRepositoryClass: ConceptGraphProperty<String> = property { initDomainRepositoryClass }
    val domainServiceClass: ConceptGraphProperty<String> = property { initDomainServiceClass }
    val domainDescription: ConceptGraphProperty<String> = property { initDomainDescription }
    val myBatisPlus: ConceptGraphProperty<Boolean> = property { true }

    var autoFindDomainCollectionClass = true
    var autoFindDomainRepositoryClass = true
    var autoFindDomainServiceClass = true
    var autoFindDomainDescription = true

    fun isAutoFindEnabled(): Boolean {
        return autoFindDomainCollectionClass ||
                autoFindDomainRepositoryClass ||
                autoFindDomainServiceClass ||
                autoFindDomainDescription
    }

    companion object {

        @JvmStatic
        val RECENTS_KEY_MODULE_PACKAGE = "ConceptCloud@ModulePackage"

        @JvmStatic
        val RECENTS_KEY_MODULE_DOMAIN_OBJECT_CLASS = "ConceptCloud@ModuleDomainObjectClass"

        @JvmStatic
        val RECENTS_KEY_MODULE_DOMAIN_COLLECTION_CLASS = "ConceptCloud@ModuleDomainCollectionClass"

        @JvmStatic
        val RECENTS_KEY_MODULE_DOMAIN_SERVICE_CLASS = "ConceptCloud@ModuleDomainServiceClass"

        @JvmStatic
        val RECENTS_KEY_MODULE_DOMAIN_REPOSITORY_CLASS = "ConceptCloud@ModuleDomainRepositoryClass"

        @JvmStatic
        fun getClassesInPackage(project: Project, psiClass: PsiClass?): Array<PsiClass> {
            if (psiClass == null) {
                return arrayOf()
            }
            val packageName = PsiUtil.getPackageName(psiClass) ?: return arrayOf()
            val psiPackage = JavaPsiFacade.getInstance(project).findPackage(packageName)
                ?: return arrayOf()
            return psiPackage.classes
        }

        @JvmStatic
        fun getDomainCollectionClass(classes: Array<PsiClass>): PsiClass? {
            return ConceptCloudUtils.getClassPredicateInterface(classes) { psiInterface ->
                TYPE_DOMAIN_COLLECTION == psiInterface.qualifiedName
            }
        }

        @JvmStatic
        fun getDomainRepositoryClass(classes: Array<PsiClass>): PsiClass? {
            return ConceptCloudUtils.getClassPredicateInterface(classes) { psiInterface ->
                TYPE_DOMAIN_REPOSITORY == psiInterface.qualifiedName
            }
        }

        @JvmStatic
        fun getClassByName(classes: Array<PsiClass>, className: String): PsiClass? {
            for (psiClass in classes) {
                if (className == psiClass.name) {
                    return psiClass
                }
            }
            return null
        }

        @JvmStatic
        fun getDomainDescriptionByComment(psiClass: PsiClass?): String {
            if (psiClass == null) {
                return ""
            }
            val comment = psiClass.docComment ?: return ""
            for (child in comment.children) {
                if (child is PsiDocToken) {
                    if (child.tokenType === JavaDocTokenType.DOC_COMMENT_DATA) {
                        return child.getText().trim { it <= ' ' }
                    }
                }
            }
            return ""
        }

        @JvmStatic
        fun getQualifiedName(psiClass: PsiClass?): String {
            return (psiClass ?: return "").qualifiedName ?: return ""
        }
    }
}

fun <T> ModuleModel.property(init: () -> T): ConceptGraphProperty<T> {
    return ConceptGraphPropertyImpl(this.propertyGraph, init)
}