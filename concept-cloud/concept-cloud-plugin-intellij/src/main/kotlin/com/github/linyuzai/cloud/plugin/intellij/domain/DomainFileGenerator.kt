package com.github.linyuzai.cloud.plugin.intellij.domain

import com.github.linyuzai.cloud.plugin.intellij.builder._java
import com.github.linyuzai.cloud.plugin.intellij.builder.lowercaseFirst
import com.github.linyuzai.cloud.plugin.intellij.builder.toGetter
import com.github.linyuzai.cloud.plugin.intellij.builder.toSampleName
import java.io.File

object DomainFileGenerator {

    @JvmStatic
    fun generate(model: DomainModel, dir: File) {
        val domainPackage = model.domainPackage.get()
        val domainObjectClassName = model.domainObjectClassName.get()
        _java("$domainObjectClassName.java") {
            _package(domainPackage)
            _interface(domainObjectClassName) {
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

        val domainCollectionClassName = model.domainCollectionClassName.get()
        _java("$domainCollectionClassName.java") {
            _package(domainPackage)
            _interface(domainCollectionClassName) {
                _public()
                _extends(
                    "com.github.linyuzai.domain.core.DomainCollection",
                    domainObjectClassName
                )
            }
        }.writeTo(dir)

        val domainRepositoryClassName = "${domainObjectClassName}Repository"
        _java("$domainRepositoryClassName.java") {
            _package(domainPackage)
            _interface(domainRepositoryClassName) {
                _public()
                _extends(
                    "com.github.linyuzai.domain.core.DomainRepository",
                    domainObjectClassName, domainCollectionClassName
                )
            }
        }.writeTo(dir)

        val stringType = "java.lang.String"

        val domainObjectImplClassName = "${domainObjectClassName}Impl"
        _java("$domainObjectImplClassName.java") {
            _package(domainPackage)
            _class(domainObjectImplClassName) {
                _public()
                _implements(domainObjectClassName)

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
                    _type(stringType)
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
                        domainObjectImplClassName
                    )

                    val notNullAnnotation = "javax.validation.constraints.NotNull"
                    val notEmptyAnnotation = "javax.validation.constraints.NotEmpty"

                    _field("id") {
                        _protected()
                        _type(stringType)
                        _annotation(notEmptyAnnotation)
                    }
                    model.domainProps.forEach {
                        _field(it.propName.get()) {
                            _protected()
                            _type(it.propClass.get())
                            if (it.propNotNull.get()) {
                                _annotation(notNullAnnotation)
                            }
                            if (it.propNotEmpty.get()) {
                                _annotation(notEmptyAnnotation)
                            }
                        }
                    }

                    _method("id") {
                        _public()
                        _param(stringType, "id")
                        _return("Builder")
                        _body("this.id=id;", "return this;")
                    }

                    model.domainProps.forEach {
                        val mn = it.propName.get()
                        _method(mn) {
                            _public()
                            _param(it.propClass.get(), mn)
                            _return("Builder")
                            _comment(it.propComment.get())
                            _body("this.$mn=$mn;", "return this;")
                        }
                    }

                    _method("build") {
                        _override()
                        _protected()
                        _return(domainObjectImplClassName)
                        val args = model.domainProps.joinToString(",") { it.propName.get() }
                        _body("return new $domainObjectImplClassName(id,$args);")
                    }
                }
            }
        }.writeTo(dir)

        val eventDir = File(dir, "event")

        val eventPackage = "$domainPackage.event"

        val getterAnnotation = "lombok.Getter"
        val requiredArgsConstructorAnnotation = "lombok.RequiredArgsConstructor"

        val domainObjectParam = domainObjectClassName.lowercaseFirst()
        val newDomainObjectParam = "new$domainObjectClassName"
        val oldDomainObjectParam = "old$domainObjectClassName"
        val userClassName = model.userClass.get()
        val userParam = userClassName.toSampleName().lowercaseFirst()

        val createEventClassName = "${domainObjectClassName}CreatedEvent"
        _java("$createEventClassName.java") {
            _package(eventPackage)
            _import("$domainPackage.$domainObjectClassName")
            _class(createEventClassName) {
                _public()

                _annotation(getterAnnotation)
                _annotation(requiredArgsConstructorAnnotation)

                _field(domainObjectParam) {
                    _protected()
                    _final()
                    _type(domainObjectClassName)
                }

                _field(userParam) {
                    _protected()
                    _final()
                    _type(userClassName)
                }
            }
        }.writeTo(eventDir)

        val updateEventClassName = "${domainObjectClassName}UpdatedEvent"
        _java("$updateEventClassName.java") {
            _package(eventPackage)
            _import("$domainPackage.$domainObjectClassName")
            _class(updateEventClassName) {
                _public()

                _annotation(getterAnnotation)
                _annotation(requiredArgsConstructorAnnotation)

                _field(newDomainObjectParam) {
                    _protected()
                    _final()
                    _type(domainObjectClassName)
                }

                _field(oldDomainObjectParam) {
                    _protected()
                    _final()
                    _type(domainObjectClassName)
                }

                _field(userParam) {
                    _protected()
                    _final()
                    _type(userClassName)
                }
            }
        }.writeTo(eventDir)

        val deleteEventClassName = "${domainObjectClassName}DeletedEvent"
        _java("$deleteEventClassName.java") {
            _package(eventPackage)
            _import("$domainPackage.$domainObjectClassName")
            _class(deleteEventClassName) {
                _public()

                _annotation(getterAnnotation)
                _annotation(requiredArgsConstructorAnnotation)

                _field(domainObjectParam) {
                    _protected()
                    _final()
                    _type(domainObjectClassName)
                }

                _field(userParam) {
                    _protected()
                    _final()
                    _type(userClassName)
                }
            }
        }.writeTo(eventDir)

        val domainServiceClassName = "${domainObjectClassName}Service"
        _java("$domainServiceClassName.java") {
            _package(domainPackage)
            _class(domainServiceClassName) {
                _public()

                val autowiredAnnotation = "org.springframework.beans.factory.annotation.Autowired"

                val domainRepositoryParam = domainRepositoryClassName.lowercaseFirst()

                _annotation("org.springframework.stereotype.Service")
                _field(domainRepositoryParam) {
                    _protected()
                    _type(domainRepositoryClassName)
                    _annotation(autowiredAnnotation)
                }

                _field("eventPublisher") {
                    _protected()
                    _type("com.github.linyuzai.domain.core.DomainEventPublisher")
                    _annotation(autowiredAnnotation)
                }

                _method("create") {
                    _public()
                    _return("void")
                    _param(domainObjectClassName, domainObjectParam)
                    _param(userClassName, userParam)
                    val args = "$domainObjectParam,$userParam"
                    _body(
                        "$domainRepositoryParam.create($domainObjectParam);",
                        "eventPublisher.publish(new $createEventClassName($args));"
                    )
                    _import("$eventPackage.$createEventClassName")
                }

                _method("update") {
                    _public()
                    _return("void")

                    _param(domainObjectClassName, newDomainObjectParam)
                    _param(domainObjectClassName, oldDomainObjectParam)
                    _param(userClassName, userParam)

                    val args = "$newDomainObjectParam,$oldDomainObjectParam,$userParam"

                    _body(
                        "$domainRepositoryParam.update($newDomainObjectParam);",
                        "eventPublisher.publish(new $updateEventClassName($args));"
                    )

                    _import("$eventPackage.$updateEventClassName")
                }

                _method("delete") {
                    _public()
                    _return("void")
                    _param(domainObjectClassName, domainObjectParam)
                    _param(userClassName, userParam)
                    val args = "$domainObjectParam,$userParam"
                    _body(
                        "$domainRepositoryParam.delete($domainObjectParam);",
                        "eventPublisher.publish(new $deleteEventClassName($args));"
                    )

                    _import("$eventPackage.$deleteEventClassName")
                }
            }
        }.writeTo(dir)
    }
}