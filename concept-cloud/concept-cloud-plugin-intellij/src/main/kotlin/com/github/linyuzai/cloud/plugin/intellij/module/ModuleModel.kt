package com.github.linyuzai.cloud.plugin.intellij.module

import com.intellij.openapi.module.Module
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.observable.properties.GraphPropertyImpl
import com.intellij.openapi.observable.properties.PropertyGraph

data class ModuleModel(
    val initUserClass: String,
    val initModuleModule: Module?,
    val initModulePackage: String,
    val initDomainObjectClass: String,
    val initDomainCollectionClass: String,
    val initDomainServiceClass: String,
    val initDomainRepositoryClass: String
) {
    val propertyGraph: PropertyGraph = PropertyGraph()
    val userClass: GraphProperty<String> = property { initUserClass }
    val moduleModule: GraphProperty<Module?> = property { initModuleModule }
    val modulePackage: GraphProperty<String> = property { initModulePackage }
    val domainObjectClass: GraphProperty<String> = property { initDomainObjectClass }
    val domainCollectionClass: GraphProperty<String> = property { initDomainCollectionClass }
    val domainServiceClass: GraphProperty<String> = property { initDomainServiceClass }
    val domainRepositoryClass: GraphProperty<String> = property { initDomainRepositoryClass }
    val myBatisPlus: GraphProperty<Boolean> = property { true }

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
    }
}

fun <T> ModuleModel.property(init: () -> T): GraphProperty<T> {
    return GraphPropertyImpl(this.propertyGraph, init)
}