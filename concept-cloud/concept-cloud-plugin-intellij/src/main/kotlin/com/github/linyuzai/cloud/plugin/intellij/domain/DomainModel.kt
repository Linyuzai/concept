package com.github.linyuzai.cloud.plugin.intellij.domain

import com.intellij.openapi.module.Module
import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.observable.properties.GraphPropertyImpl
import com.intellij.openapi.observable.properties.PropertyGraph
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Consumer

data class DomainModel(
    val initUserClass: String,
    val initDomainModule: Module?,
    val initDomainPackage: String,
    val initDomainClassName: String
) {
    val propertyGraph: PropertyGraph = PropertyGraph()
    val userClassProperty: GraphProperty<String> = property { initUserClass }
    val domainModuleProperty: GraphProperty<Module?> = property { initDomainModule }
    val domainPackageProperty: GraphProperty<String> = property { initDomainPackage }
    val domainClassNameProperty: GraphProperty<String> = property { initDomainClassName }
    val domainPreviewProperty: GraphProperty<String> = property(false) { "" }

    private val domainProps: MutableList<DomainProp> = CopyOnWriteArrayList()
    private val onDomainPropAddListeners: MutableCollection<Consumer<DomainProp>> = CopyOnWriteArrayList()
    private val onDomainPropRemoveListeners: MutableCollection<Consumer<DomainProp>> = CopyOnWriteArrayList()

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

    fun preview() {
        val imports = mutableSetOf<String>()
        imports.add(userClassProperty.get())
        val fields = StringBuilder()
        for (prop in domainProps) {
            val className = prop.propClass.get()

            imports.add(className)

            val lastIndexOf = className.lastIndexOf(".")
            fields.append("  private ")
                .append(
                    if (lastIndexOf > 0 && className.length > 1) {
                        className.substring(lastIndexOf + 1)
                    } else {
                        className
                    }
                )
                .append(" ")
                .append(prop.propName.get())
                .append(";\n\n")
        }
        val text = """
package ${domainPackageProperty.get()};

${imports.filter { !it.startsWith("java.lang.") }.joinToString(";\nimport ", "import ", ";\n")}
public class ${domainClassNameProperty.get()} {
                
$fields
}"""
        domainPreviewProperty.set(text)
    }
}

data class DomainProp(
    val model: DomainModel,
    var index: Int,
    var propClass: GraphProperty<String> = model.property { "" },
    var propName: GraphProperty<String> = model.property { "" },
    var propNotNull: GraphProperty<Boolean> = model.property { false },
    var propNotEmpty: GraphProperty<Boolean> = model.property { false },
    var propComment: GraphProperty<String> = model.property { "" },
    var smartFill: Boolean = true,
    var onClassNameUpdateListener: ((String) -> Unit)? = null
)

fun <T> DomainModel.property(preview: Boolean = true, init: () -> T): GraphProperty<T> {
    return GraphPropertyImpl(this.propertyGraph, init).apply {
        if (preview) {
            afterChange {
                preview()
            }
        }
    }
}