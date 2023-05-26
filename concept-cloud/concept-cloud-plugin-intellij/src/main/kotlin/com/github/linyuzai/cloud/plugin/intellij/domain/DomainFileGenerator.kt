package com.github.linyuzai.cloud.plugin.intellij.domain

import com.github.linyuzai.cloud.plugin.intellij.builder._java
import com.github.linyuzai.cloud.plugin.intellij.builder.toGetter
import com.github.linyuzai.cloud.plugin.intellij.builder.toSampleName
import java.io.File

object DomainFileGenerator {

    @JvmStatic
    fun generate(model: DomainModel, dir: File) {
        val domainObjectName = model.domainObjectClassName.get()
        _java("$domainObjectName.java") {
            _package(model.domainPackage.get())
            _interface(domainObjectName) {
                _public()
                _extends("com.github.linyuzai.domain.core.DomainEntity")
                model.domainProps.forEach {
                    val pClass = it.propClass.get()
                    _method(it.propName.get().toGetter("boolean" == pClass)) {
                        _return(pClass)
                        _comment(it.propComment.get())
                    }
                }
                _comment(model.domainClassComment.get())
            }
        }.writeTo(dir)

        val domainCollectionName = model.domainCollectionClassName.get()
        _java("$domainCollectionName.java") {
            _package(model.domainPackage.get())
            _interface(domainCollectionName) {
                _public()
                _extends(
                    "com.github.linyuzai.domain.core.DomainCollection",
                    domainObjectName
                )
            }
        }.writeTo(dir)

        val domainRepositoryName = "${model.domainObjectClassName.get()}Repository"
        _java("$domainRepositoryName.java") {
            _package(model.domainPackage.get())
            _interface(domainRepositoryName) {
                _public()
                _extends(
                    "com.github.linyuzai.domain.core.DomainRepository",
                    domainObjectName, domainCollectionName
                )
            }
        }.writeTo(dir)

        val domainObjectImplName = "${model.domainObjectClassName.get()}Impl"
        _java("$domainObjectImplName.java") {
            _package(model.domainPackage.get())
            _class(domainObjectImplName) {
                _public()
                _implements(domainObjectName)

                _annotation("lombok.Getter")
                _annotation(
                    "lombok.NoArgsConstructor",
                    "access" to ("lombok.AccessLevel" to "PROTECTED")
                )
                _annotation(
                    "lombok.AllArgsConstructor",
                    "access" to ("lombok.AccessLevel" to "PROTECTED")
                )

                _field("id") {
                    _protected()
                    _type("java.lang.String")
                }

                model.domainProps.forEach {
                    _field(it.propName.get()) {
                        _protected()
                        _type(it.propClass.get())
                    }
                }

                _class("Builder") {
                    _public()
                    _static()
                    _extends(
                        "com.github.linyuzai.domain.core.AbstractDomainBuilder",
                        domainObjectImplName
                    )
                    model.domainProps.forEach {
                        _field(it.propName.get()) {
                            _protected()
                            _type(it.propClass.get())
                            if (it.propNotNull.get()) {
                                _annotation("javax.validation.constraints.NotNull")
                            }
                            if (it.propNotEmpty.get()) {
                                _annotation("javax.validation.constraints.NotEmpty")
                            }
                        }
                    }

                    model.domainProps.forEach {
                        val mn = it.propName.get()
                        _method(mn) {
                            _public()
                            _param(it.propClass.get(), mn)
                            _return("Builder")
                            _comment(it.propComment.get())
                            _body("this.$mn=$mn;return this;")
                        }
                    }

                    _method("build") {
                        _override()
                        _protected()
                        _return(domainObjectImplName)
                        val args = model.domainProps.joinToString(",") { it.propName.get() }
                        _body("return new $domainObjectImplName($args);")
                    }
                }
            }
        }.writeTo(dir)
    }
}