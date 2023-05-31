package com.github.linyuzai.cloud.plugin.intellij.domain

import com.github.linyuzai.cloud.plugin.intellij.builder.*
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
                _extends(TYPE_DOMAIN_ENTITY)
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
                _extends(TYPE_DOMAIN_COLLECTION, domainObjectClassName)
            }
        }.writeTo(dir)

        val domainRepositoryClassName = "${domainObjectClassName}Repository"
        _java("$domainRepositoryClassName.java") {
            _package(domainPackage)
            _interface(domainRepositoryClassName) {
                _public()
                _extends(
                    TYPE_DOMAIN_REPOSITORY,
                    domainObjectClassName,
                    domainCollectionClassName
                )
            }
        }.writeTo(dir)

        val domainObjectImplClassName = "${domainObjectClassName}Impl"
        _java("$domainObjectImplClassName.java") {
            _package(domainPackage)
            _class(domainObjectImplClassName) {
                _public()
                _implements(domainObjectClassName)

                _annotation(ANNOTATION_GETTER)
                _annotation(ANNOTATION_NO_ARGS_CONSTRUCTOR, PARAM_ACCESS_PROTECTED)
                _annotation(ANNOTATION_ALL_ARGS_CONSTRUCTOR, PARAM_ACCESS_PROTECTED)

                _field(PARAM_ID) {
                    _protected()
                    _type(TYPE_STRING)
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

                    _field(PARAM_ID) {
                        _protected()
                        _type(TYPE_STRING)
                        _annotation(ANNOTATION_NOT_EMPTY)
                    }
                    model.domainProps.forEach {
                        _field(it.propName.get()) {
                            _protected()
                            _type(it.propClass.get())
                            if (it.propNotNull.get()) {
                                _annotation(ANNOTATION_NOT_NULL)
                            }
                            if (it.propNotEmpty.get()) {
                                _annotation(ANNOTATION_NOT_EMPTY)
                            }
                        }
                    }

                    _method(PARAM_ID) {
                        _public()
                        _param(PARAM_ID) {
                            _type(TYPE_STRING)
                        }
                        _return("Builder")
                        _body("this.id=id;", "return this;")
                    }

                    model.domainProps.forEach {
                        val mn = it.propName.get()
                        _method(mn) {
                            _public()
                            _param(mn) {
                                _type(it.propClass.get())
                            }
                            _return("Builder")
                            _comment(it.propComment.get())
                            _body("this.$mn=$mn;", "return this;")
                        }
                    }

                    _method("build") {
                        _override()
                        _protected()
                        _return(domainObjectImplClassName)
                        val argList = model.domainProps.map { it.propName.get() }.toMutableList()
                        argList.add(0, "id")
                        val args = argList.joinToString(",")
                        _body("return new $domainObjectImplClassName($args);")
                    }
                }
            }
        }.writeTo(dir)

        val eventDir = File(dir, "event")

        val eventPackage = "$domainPackage.event"

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

                _annotation(ANNOTATION_GETTER)
                _annotation(ANNOTATION_REQUIRED_ARGS_CONSTRUCTOR)

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

                _annotation(ANNOTATION_GETTER)
                _annotation(ANNOTATION_REQUIRED_ARGS_CONSTRUCTOR)

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

                _annotation(ANNOTATION_GETTER)
                _annotation(ANNOTATION_REQUIRED_ARGS_CONSTRUCTOR)

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

                val domainRepositoryParam = domainRepositoryClassName.lowercaseFirst()

                _annotation(ANNOTATION_SERVICE)
                _field(domainRepositoryParam) {
                    _protected()
                    _type(domainRepositoryClassName)
                    _annotation(ANNOTATION_AUTOWIRED)
                }

                _field("eventPublisher") {
                    _protected()
                    _type(TYPE_DOMAIN_EVENT_PUBLISHER)
                    _annotation(ANNOTATION_AUTOWIRED)
                }

                _method("create") {
                    _public()
                    _param(domainObjectParam) {
                        _type(domainObjectClassName)
                    }
                    _param(userParam) {
                        _type(userClassName)
                    }
                    val args = "$domainObjectParam,$userParam"
                    _body(
                        "$domainRepositoryParam.create($domainObjectParam);",
                        "eventPublisher.publish(new $createEventClassName($args));"
                    )
                    _import("$eventPackage.$createEventClassName")
                }

                _method("update") {
                    _public()

                    _param(newDomainObjectParam) {
                        _type(domainObjectClassName)
                    }
                    _param(oldDomainObjectParam) {
                        _type(domainObjectClassName)
                    }
                    _param(userParam) {
                        _type(userClassName)
                    }

                    val args = "$newDomainObjectParam,$oldDomainObjectParam,$userParam"

                    _body(
                        "$domainRepositoryParam.update($newDomainObjectParam);",
                        "eventPublisher.publish(new $updateEventClassName($args));"
                    )

                    _import("$eventPackage.$updateEventClassName")
                }

                _method("delete") {
                    _public()

                    _param(domainObjectParam) {
                        _type(domainObjectClassName)
                    }
                    _param(userParam) {
                        _type(userClassName)
                    }
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