package com.github.linyuzai.cloud.plugin.intellij.domain

data class DomainProp(
    var index: Int,
    var domainClassName: String = "",
    var domainPropName: String = "",
    var smartFill: Boolean = true,
    var onClassNameUpdateListener: ((String) -> Unit)? = null
) {
    constructor(index: Int) : this(index, "")
}