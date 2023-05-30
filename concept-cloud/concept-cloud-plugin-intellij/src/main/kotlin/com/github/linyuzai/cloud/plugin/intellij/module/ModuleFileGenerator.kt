package com.github.linyuzai.cloud.plugin.intellij.module

import com.github.linyuzai.cloud.plugin.intellij.builder.*
import java.io.File

object ModuleFileGenerator {

    @JvmStatic
    fun generate(model: ModuleModel, dir: File) {

        val modulePackage = model.modulePackage.get()
        val domainObjectClass = model.domainObjectClass.get()
        val desc = model.domainDescription.get()
        val domainObjectClassName = domainObjectClass.toSampleName()
        val domainObjectParam = domainObjectClassName.lowercaseFirst()

        val viewPackage = "$modulePackage.view"
        val viewDir = File(dir, "view")

        val createCommand = "${domainObjectClassName}CreateCommand"
        _java("$createCommand.java") {
            _package(viewPackage)
            _class(createCommand) {
                _public()
                _annotation(ANNOTATION_DATA)
                _annotation(ANNOTATION_SCHEMA, paramDesc("${desc}创建命令"))
            }
        }.writeTo(viewDir)

        val updateCommand = "${domainObjectClassName}UpdateCommand"
        _java("$updateCommand.java") {
            _package(viewPackage)
            _class(updateCommand) {
                _public()
                _annotation(ANNOTATION_DATA)
                _annotation(ANNOTATION_SCHEMA, paramDesc("${desc}更新命令"))
                _field(PARAM_ID) {
                    _protected()
                    _type(TYPE_STRING)
                    _annotation(ANNOTATION_SCHEMA, paramDesc("${desc}ID"))
                }
            }
        }.writeTo(viewDir)

        val deleteCommand = "${domainObjectClassName}DeleteCommand"
        _java("$deleteCommand.java") {
            _package(viewPackage)
            _class(deleteCommand) {
                _public()
                _annotation(ANNOTATION_DATA)
                _annotation(ANNOTATION_SCHEMA, paramDesc("${desc}删除命令"))
                _field(PARAM_ID) {
                    _protected()
                    _type(TYPE_STRING)
                    _annotation(ANNOTATION_SCHEMA, paramDesc("${desc}ID"))
                }
            }
        }.writeTo(viewDir)

        val vo = "${domainObjectClassName}VO"
        _java("$vo.java") {
            _package(viewPackage)
            _class(vo) {
                _public()
                _annotation(ANNOTATION_DATA)
                _annotation(ANNOTATION_SCHEMA, paramDesc("${desc}视图"))
                _field(PARAM_ID) {
                    _protected()
                    _type(TYPE_STRING)
                    _annotation(ANNOTATION_SCHEMA, paramDesc("${desc}ID"))
                }
            }
        }.writeTo(viewDir)

        val query = "${domainObjectClassName}Query"
        _java("$query.java") {
            _package(viewPackage)
            _class(query) {
                _public()
                _annotation(ANNOTATION_DATA)
                _annotation(ANNOTATION_SCHEMA, paramDesc("${desc}查询条件"))
            }
        }.writeTo(viewDir)

        val idGenerator = "${domainObjectClassName}IdGenerator"
        _java("$idGenerator.java") {
            _package(modulePackage)
            _interface(idGenerator) {
                _public()
                _comment("${desc}ID生成器")
                _extends(TYPE_DOMAIN_ID_GENERATOR, "$viewPackage.$createCommand")

            }
        }

        val facadeAdapter = "${domainObjectClassName}FacadeAdapter"
        _java("$facadeAdapter.java") {
            _package(modulePackage)
            _interface(facadeAdapter) {
                _public()
                _comment("领域模型/视图 转换适配器")

                _method("from") {
                    _return(domainObjectClass)
                    _param("$viewPackage.$createCommand", "create")
                    _comment("创建命令视图转领域模型")
                }

                _method("from") {
                    _return(domainObjectClass)
                    _param("$viewPackage.$updateCommand", "update")
                    _param(domainObjectClass, "old")
                    _comment("更新命令视图转领域模型")
                }

                _method("do2vo") {
                    _return("$viewPackage.$vo")
                    _param(domainObjectClass, domainObjectParam)
                    _comment("领域模型转视图")
                }

                _method("toConditions") {
                    _return(TYPE_DOMAIN_CONDITIONS)
                    _param("$viewPackage.$query", "query")
                    _comment("查询转条件")
                }
            }
        }.writeTo(dir)

        /*val facadeAdapterImpl = "${facadeAdapter}Impl"
        _java("$facadeAdapterImpl.java") {
            _package(modulePackage)
            _class(facadeAdapterImpl) {
                _public()
                _comment("领域模型/视图 转换适配器实现")
                _annotation(ANNOTATION_COMPONENT)

                _implements(facadeAdapter)

                _method("from") {
                    _override()
                    _return(domainObjectClass)
                    _param("$viewPackage.$createCommand", "create")
                    _comment("创建命令视图转领域模型")
                }

                _method("from") {
                    _return(domainObjectClass)
                    _param("$viewPackage.$updateCommand", "update")
                    _param(domainObjectClass, "old")
                    _comment("更新命令视图转领域模型")
                }

                _method("do2vo") {
                    _return("$viewPackage.$vo")
                    _param(domainObjectClass, domainObjectParam)
                    _comment("领域模型转视图")
                }

                _method("toConditions") {
                    _return(TYPE_DOMAIN_CONDITIONS)
                    _param("$viewPackage.$query", "query")
                    _comment("查询转条件")
                }
            }
        }.writeTo(dir)*/
    }
}