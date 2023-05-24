package com.github.linyuzai.cloud.plugin.intellij.domain

import com.intellij.openapi.module.Module
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.observable.properties.GraphPropertyImpl
import com.intellij.openapi.observable.properties.PropertyGraph
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer

data class DomainModel(
    var initUserClassName: String,
    var initDomainModule: Module?,
    var initDomainPackage: String,
    var initDomainName: String,
    val propertyGraph: PropertyGraph = PropertyGraph(),
    val userClassNameProperty: GraphProperty<String> = GraphPropertyImpl(propertyGraph) { initUserClassName },
    val domainModuleProperty: GraphProperty<Module?> = GraphPropertyImpl(propertyGraph) { initDomainModule },
    val domainPackageProperty: GraphProperty<String> = GraphPropertyImpl(propertyGraph) { initDomainPackage },
    val domainNameProperty: GraphProperty<String> = GraphPropertyImpl(propertyGraph) { initDomainName },
    private val domainProps: MutableList<DomainProp> = CopyOnWriteArrayList(),
    private val onDomainPropAddListeners: MutableCollection<Consumer<DomainProp>> = CopyOnWriteArrayList(),
    private val onDomainPropRemoveListeners: MutableCollection<Consumer<DomainProp>> = CopyOnWriteArrayList()
) {
    companion object {

        @JvmStatic
        fun create(
            userClassName: String,
            domainModule: Module?,
            domainPackage: String,
            domainName: String
        ): DomainModel = DomainModel(userClassName, domainModule, domainPackage, domainName)
    }

    fun addDomainProp(): DomainProp {
        val prop = DomainProp(this, domainProps.size)
        domainProps.add(prop)
        for (onDomainPropAddListener in onDomainPropAddListeners) {
            onDomainPropAddListener.accept(prop)
        }
        return prop
    }

    fun removeDomainProp(index: Int) {
        val remove = domainProps.removeAt(index)
        for (i in index until domainProps.size) {
            domainProps[i].index = i
        }
        for (onDomainPropRemoveListener in onDomainPropRemoveListeners) {
            onDomainPropRemoveListener.accept(remove)
        }
    }

    fun addOnDomainPropAddListener(listener: Consumer<DomainProp>) {
        onDomainPropAddListeners.add(listener)
    }

    fun addOnDomainPropRemoveListener(listener: Consumer<DomainProp>) {
        onDomainPropRemoveListeners.add(listener)
    }
}