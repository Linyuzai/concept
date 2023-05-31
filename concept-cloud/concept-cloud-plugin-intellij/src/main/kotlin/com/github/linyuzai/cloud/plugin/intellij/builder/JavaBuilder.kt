package com.github.linyuzai.cloud.plugin.intellij.builder

import com.intellij.openapi.util.io.FileUtil
import java.io.File

const val ANNOTATION_NOT_NULL = "javax.validation.constraints.NotNull"
const val ANNOTATION_NOT_EMPTY = "javax.validation.constraints.NotEmpty"
const val ANNOTATION_SERVICE = "org.springframework.stereotype.Service"
const val ANNOTATION_COMPONENT = "org.springframework.stereotype.Component"
const val ANNOTATION_AUTOWIRED = "org.springframework.beans.factory.annotation.Autowired"
const val ANNOTATION_GETTER = "lombok.Getter"
const val ANNOTATION_DATA = "lombok.Data"
const val ANNOTATION_NO_ARGS_CONSTRUCTOR = "lombok.NoArgsConstructor"
const val ANNOTATION_ALL_ARGS_CONSTRUCTOR = "lombok.AllArgsConstructor"
const val ANNOTATION_REQUIRED_ARGS_CONSTRUCTOR = "lombok.RequiredArgsConstructor"
const val ANNOTATION_SCHEMA = "io.swagger.v3.oas.annotations.media.Schema"

const val TYPE_VOID = "void"
const val TYPE_STRING = "java.lang.String"
const val TYPE_LIST = "java.util.List"
const val TYPE_COLLECTORS = "java.util.stream.Collectors"
const val TYPE_DOMAIN_VALUE = "com.github.linyuzai.domain.core.DomainValue"
const val TYPE_DOMAIN_ENTITY = "com.github.linyuzai.domain.core.DomainEntity"
const val TYPE_DOMAIN_COLLECTION = "com.github.linyuzai.domain.core.DomainCollection"
const val TYPE_DOMAIN_REPOSITORY = "com.github.linyuzai.domain.core.DomainRepository"
const val TYPE_DOMAIN_ID_GENERATOR = "com.github.linyuzai.domain.core.DomainIdGenerator"
const val TYPE_DOMAIN_VALIDATOR = "com.github.linyuzai.domain.core.DomainValidator"
const val TYPE_DOMAIN_EVENT_PUBLISHER = "com.github.linyuzai.domain.core.DomainEventPublisher"
const val TYPE_PAGES = "com.github.linyuzai.domain.core.page.Pages"
const val TYPE_DOMAIN_CONDITIONS = "com.github.linyuzai.domain.core.condition.Conditions"
const val TYPE_DOMAIN_LAMBDA_CONDITIONS = "com.github.linyuzai.domain.core.condition.LambdaConditions"
const val TYPE_DOMAIN_NOT_FOUND_EXCEPTION = "com.github.linyuzai.domain.core.exception.DomainNotFoundException"

const val PARAM_ID = "id"
const val PARAM_DESCRIPTION = "description"
val PARAM_ACCESS_PROTECTED = "access" to ("lombok.AccessLevel" to "PROTECTED")
fun schemaDescription(desc: String): Pair<String, Pair<String, String>> {
    return PARAM_DESCRIPTION to ("" to "\"$desc\"")
}

data class JavaBuilder(val _name: String) : ContentGenerator() {

    private var _package = ""

    private val _imports = mutableSetOf<String>()

    fun _package(_package: String) {
        this._package = _package
    }

    fun _import(_import: String) {
        this._imports.add(_import)
    }

    fun _interface(_name: String, _init: InterfaceBuilder.() -> Unit): InterfaceBuilder {
        val builder = InterfaceBuilder(this, _name)
        builder._init()
        add(builder)
        return builder
    }

    fun _class(_name: String, _init: ClassBuilder.() -> Unit): ClassBuilder {
        val builder = ClassBuilder(this, _name)
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
                .filter { it.isNotBlank() }
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

    private var _access = ""

    private val _interfaces = mutableListOf<Pair<String, Array<out String>>>()

    private var _comment = ""

    fun _public() {
        this._access = "public"
    }

    fun _extends(_type: String, vararg _generic: String) {
        _interfaces.add(_type to _generic)
        _java._import(_type)
        _generic.forEach {
            _java._import(it)
        }
    }

    fun _method(_name: String, _init: MethodBuilder.() -> Unit): MethodBuilder {
        val builder = MethodBuilder(_java, _name)
        builder._init()
        add(builder)
        return builder
    }

    fun _comment(_comment: String) {
        this._comment = _comment
    }

    override fun content(): String {
        return buildString {
            addComment(_comment)

            addAccess(_access)

            append("interface $_name ")

            addInterfaces("extends", _interfaces)

            append("{\n\n")
            append(children())
            append("}")
        }
    }
}

data class ClassBuilder(val _java: JavaBuilder, val _name: String) : ContentGenerator() {

    private var _access = ""

    private var _static = false

    private val _annotations = mutableListOf<Pair<String, Array<out Pair<String, Pair<String, String>>>>>()

    private var _extends = Pair<String, Array<out String>>("", emptyArray())

    private val _interfaces = mutableListOf<Pair<String, Array<out String>>>()

    private var _comment = ""

    fun _public() {
        this._access = "public"
    }

    fun _static() {
        this._static = true
    }

    fun _annotation(_type: String, vararg _params: Pair<String, Pair<String, String>>) {
        this._annotations.add(_type to _params)
        this._java._import(_type)
        _params.forEach {
            this._java._import(it.second.first)
        }
    }

    fun _extends(_type: String, vararg _generic: String) {
        this._extends = _type to _generic
        this._java._import(_type)
    }

    fun _implements(_type: String, vararg _generic: String) {
        _interfaces.add(_type to _generic)
        _java._import(_type)
    }

    fun _field(_name: String, _init: FieldBuilder.() -> Unit): FieldBuilder {
        val builder = FieldBuilder(_java, _name)
        builder._init()
        add(builder)
        return builder
    }

    fun _method(_name: String, _init: MethodBuilder.() -> Unit): MethodBuilder {
        val builder = MethodBuilder(_java, _name)
        builder._init()
        add(builder)
        return builder
    }

    fun _class(_name: String, _init: ClassBuilder.() -> Unit): ClassBuilder {
        val builder = ClassBuilder(_java, _name)
        builder._init()
        add(builder)
        return builder
    }

    fun _comment(_comment: String) {
        this._comment = _comment
    }

    override fun content(): String {
        return buildString {

            addComment(_comment)

            addAnnotations(_annotations)

            addAccess(_access)

            if (_static) {
                append("static ")
            }

            append("class $_name ")

            if (_extends.first.isNotEmpty()) {
                append("extends ")
                append(_extends.first.toSampleName())
                if (_extends.second.isNotEmpty()) {
                    append(_extends.second.joinToString(",", "<", ">")
                    { s -> s.toSampleName() })
                }
                append(" ")
            }

            addInterfaces("implements", _interfaces)

            append("{\n\n")
            append(children())
            append("}")
        }
    }
}

data class MethodBuilder(private val _java: JavaBuilder, private val _name: String) : ContentGenerator() {

    private var _access = ""

    private val _params = mutableListOf<Pair<String, String>>()

    private var _return = ""

    private val _annotations = mutableListOf<Pair<String, Array<out Pair<String, Pair<String, String>>>>>()

    private var _comment = ""

    private var _hasBody = false

    private var _body = ""

    fun _public() {
        this._access = "public"
    }

    fun _protected() {
        this._access = "protected"
    }

    fun _param(_type: String, _name: String, _import: Boolean = true) {
        this._params.add(_type to _name)
        if (_import) {
            this._java._import(_type)
        }
    }

    fun _return(_return: String, _import: Boolean = true) {
        this._return = _return
        if (_import) {
            this._java._import(_return)
        }
    }

    fun _override() {
        this._annotation("java.lang.Override")
    }

    fun _annotation(_type: String, vararg _params: Pair<String, Pair<String, String>>) {
        this._annotations.add(_type to _params)
        this._java._import(_type)
        _params.forEach {
            this._java._import(it.second.first)
        }
    }

    fun _comment(_comment: String) {
        this._comment = _comment
    }

    fun _body(_body: String) {
        this._hasBody = true
        this._body = _body
    }

    fun _body(vararg _body: String) {
        this._body(_body.joinToString("\n"))
    }

    fun _todo(_todo: String = _name, returnNull: Boolean = true) {
        val todo = "\t\t//TODO $_todo"
        if (returnNull) {
            this._body(todo, "return null;")
        } else {
            this._body(todo)
        }
    }

    override fun content(): String {
        return buildString {
            addComment(_comment)

            addAnnotations(_annotations)

            addAccess(_access)

            val args = _params.joinToString(",") { "${it.first.toSampleName()} ${it.second}" }

            append("${_return.toSampleName()} $_name($args)")

            if (_hasBody) {
                append("{\n")
                append(_body)
                append("}")
            } else {
                append(";\n")
            }
        }
    }
}

data class FieldBuilder(private val _java: JavaBuilder, private val _name: String) : ContentGenerator() {

    private var _access = ""

    private var _final = false

    private var _type = ""

    private val _annotations = mutableListOf<Pair<String, Array<out Pair<String, Pair<String, String>>>>>()

    private var _comment = ""

    fun _protected() {
        this._access = "protected"
    }

    fun _final() {
        this._final = true
    }

    fun _type(_type: String) {
        this._type = _type
        this._java._import(_type)
    }

    fun _annotation(_type: String, vararg _params: Pair<String, Pair<String, String>>) {
        this._annotations.add(_type to _params)
        this._java._import(_type)
        _params.forEach {
            this._java._import(it.second.first)
        }
    }

    fun _comment(_comment: String) {
        this._comment = _comment
    }

    override fun content(): String {
        return buildString {
            addComment(_comment)

            addAnnotations(_annotations)

            addAccess(_access)

            if (_final) {
                append("final ")
            }

            append("${_type.toSampleName()} $_name;\n")
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

inline fun _java(_name: String, init: JavaBuilder.() -> Unit): JavaBuilder {
    val builder = JavaBuilder(_name)
    builder.init()
    return builder
}

val IGNORE_SAMPLE_NAME_ARRAY = arrayOf("Pages.Args")

fun String.toSampleName() = if (IGNORE_SAMPLE_NAME_ARRAY.contains(this)) {
    this
} else {
    this.substringAfterLast(".")
}

fun String.uppercaseFirst() = this[0].uppercase() + this.substring(1)

fun String.lowercaseFirst() = this[0].lowercase() + this.substring(1)

fun String.toGetter(ifIs: Boolean): String {
    val temp = uppercaseFirst()
    return if (ifIs) {
        "is$temp"
    } else {
        "get$temp"
    }
}

fun StringBuilder.addComment(_comment: String) {
    if (_comment.isNotBlank()) {
        append("/**\n")
        append(" * $_comment\n")
        append(" */\n")
    }
}

fun StringBuilder.addAnnotations(_annotations: List<Pair<String, Array<out Pair<String, Pair<String, String>>>>>) {
    if (_annotations.isNotEmpty()) {
        _annotations.forEach {
            append("@${it.first.toSampleName()}")
            if (it.second.isNotEmpty()) {
                append(it.second.joinToString(",", "(", ")") { param ->
                    buildString {
                        if (param.first.isNotBlank()) {
                            append("${param.first}=")
                        }
                        if (param.second.first.isEmpty()) {
                            append(param.second.second)
                        } else {
                            append("${param.second.first.toSampleName()}.${param.second.second}")
                        }
                    }
                })
            }
        }
        append("\n")
    }
}

fun StringBuilder.addAccess(_access: String) {
    if (_access.isNotBlank()) {
        append("$_access ")
    }
}

fun StringBuilder.addInterfaces(implementsOrExtends: String, _interfaces: List<Pair<String, Array<out String>>>) {
    if (_interfaces.isNotEmpty()) {
        append("$implementsOrExtends ")
        _interfaces.forEach {
            append(it.first.toSampleName())
            if (it.second.isNotEmpty()) {
                append(it.second.joinToString(",", "<", ">")
                { s -> s.toSampleName() })
            }
        }
        append(" ")
    }
}


