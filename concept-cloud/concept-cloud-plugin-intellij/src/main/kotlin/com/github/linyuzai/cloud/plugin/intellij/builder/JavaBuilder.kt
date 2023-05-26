package com.github.linyuzai.cloud.plugin.intellij.builder

import com.intellij.openapi.util.io.FileUtil
import java.io.File

data class JavaBuilder(val _name: String) : ContentGenerator() {

    var _package: String = ""

    val _imports = mutableSetOf<String>()

    fun _package(_package: String) {
        this._package = _package
    }

    fun _interface(_name: String, _init: InterfaceBuilder.() -> Unit): InterfaceBuilder {
        val builder = InterfaceBuilder(this, _name)
        builder._init()
        add(builder)
        return builder
    }

    fun writeTo(dir: File) {
        val file = File(dir, _name)
        FileUtil.writeToFile(file, content())
    }

    override fun content(): String {
        return buildString {
            append("package $_package;\n")
            _imports.filterNot { it.startsWith("java.lang.") }
                .filter { it.contains(".") }
                .sorted()
                .forEach {
                    append("import $it;\n")
                }
            append(children())
        }
    }
}

data class InterfaceBuilder(val _java: JavaBuilder, val _name: String) : ContentGenerator() {

    private val _interfaces = mutableListOf<Pair<String, Array<out String>>>()

    fun _extends(_type: String, vararg _generic: String) {
        _interfaces.add(_type to _generic)
        _java._imports.add(_type)
    }

    fun _extendsDomainEntity() {
        _extends("com.github.linyuzai.domain.core.DomainEntity")
    }

    fun _method(_name: String, _init: MethodBuilder.() -> Unit): MethodBuilder {
        val builder = MethodBuilder(_java, _name)
        builder._init()
        add(builder)
        return builder
    }

    override fun content(): String {
        return buildString {
            append("public interface $_name ")
            if (_interfaces.isNotEmpty()) {
                append("extends ")
                _interfaces.forEach {
                    append(it.first.toSampleName())
                    if (it.second.isNotEmpty()) {
                        append(it.second.joinToString(",", "<", ">")
                        { s -> s.toSampleName() })
                    }
                }
            }
            append("{\n\n")
            append(children())
            append("}")
        }
    }
}

data class ClassBuilder(val _name: String) {

    fun _implement(_type: String, vararg _generic: String) {

    }

    fun _implementDomainEntity() {
        _implement("com.github.linyuzai.domain.core.DomainEntity")
    }

    fun annotations(vararg type: String) {

    }

    fun annotation() {

    }

    fun field(modifier: String, name: String, type: String) {

    }

    fun fieldProtected(name: String, type: String) {
        field("protected", name, type)
    }
}

data class MethodBuilder(val _java: JavaBuilder, val _name: String) : ContentGenerator() {

    var _return: String = ""

    var _comment: String = ""

    var _hasBody: Boolean = false

    fun _return(_return: String) {
        this._return = _return
        _java._imports.add(_return)
    }

    fun _comment(_comment: String) {
        this._comment = _comment
    }

    fun _body(_body: String) {
        _hasBody = true
    }

    fun annotation(type: String) {

    }

    fun annotation() {

    }

    override fun content(): String {
        return buildString {
            if (_comment.isNotBlank()) {
                append("/**\n")
                append(" * $_comment\n")
                append(" */\n")
            }
            _return.toSampleName().apply {
                append("$this ${_name.toGetter("boolean" == this)}()")
            }

            if (_hasBody) {
                append("{\n")
                append(children())
                append("}")
            } else {
                append(";\n")
            }
        }
    }
}

abstract class ContentGenerator {

    private val contents = mutableListOf<ContentGenerator>()

    fun add(content: ContentGenerator) {
        contents.add(content)
    }

    abstract fun content(): String

    fun children(): String {
        return buildString {
            contents.forEach {
                append(it.content())
            }
        }
    }
}

inline fun _java(_package: String, init: JavaBuilder.() -> Unit): JavaBuilder {
    val builder = JavaBuilder(_package)
    builder.init()
    return builder
}

fun String.toSampleName() = this.substringAfterLast(".")

fun String.toGetter(ifIs: Boolean): String {
    val temp = this[0].uppercase() + this.substring(1)
    return if (ifIs) {
        "is$temp"
    } else {
        "get$temp"
    }
}

