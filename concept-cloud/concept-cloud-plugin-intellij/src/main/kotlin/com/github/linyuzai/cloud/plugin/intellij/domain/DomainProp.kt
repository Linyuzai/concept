package com.github.linyuzai.cloud.plugin.intellij.domain

import com.intellij.openapi.observable.properties.GraphProperty
import com.intellij.openapi.observable.properties.GraphPropertyImpl

data class DomainProp(
    val model: DomainModel,
    var index: Int,
    var className: GraphProperty<String> = GraphPropertyImpl(model.propertyGraph) { "" },
    var propName: GraphProperty<String> = GraphPropertyImpl(model.propertyGraph) { "" },
    var propNotNull: GraphProperty<Boolean> = GraphPropertyImpl(model.propertyGraph) { false },
    var propNotEmpty: GraphProperty<Boolean> = GraphPropertyImpl(model.propertyGraph) { false },
    var propComment: GraphProperty<String> = GraphPropertyImpl(model.propertyGraph) { "" },
    var smartFill: Boolean = true,
    var onClassNameUpdateListener: ((String) -> Unit)? = null
)