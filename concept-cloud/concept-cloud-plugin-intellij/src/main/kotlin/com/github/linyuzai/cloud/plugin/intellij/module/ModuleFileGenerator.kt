package com.github.linyuzai.cloud.plugin.intellij.module

import com.github.linyuzai.cloud.plugin.intellij.builder.*
import java.io.File

object ModuleFileGenerator {

    @JvmStatic
    fun generate(model: ModuleModel, dir: File) {
        val userClass = model.userClass.get()
        val modulePackage = model.modulePackage.get()
        val domainObjectClass = model.domainObjectClass.get()
        val domainObjectClassName = domainObjectClass.toSampleName()
        val domainObjectParam = domainObjectClassName.lowercaseFirst()
        val domainServiceClass = model.domainServiceClass.get()
        val domainServiceClassName = domainServiceClass.toSampleName()
        val domainServiceParam = domainServiceClassName.lowercaseFirst()
        val domainRepositoryClass = model.domainRepositoryClass.get()
        val domainRepositoryClassName = domainRepositoryClass.toSampleName()
        val domainRepositoryParam = domainRepositoryClassName.lowercaseFirst()

        val description = model.domainDescription.get()

        val viewPackage = "$modulePackage.view"
        val viewDir = File(dir, "view")

        val createCommand = "${domainObjectClassName}CreateCommand"
        _java("$createCommand.java") {
            _package(viewPackage)
            _class(createCommand) {
                _public()
                _annotation(ANNOTATION_DATA)
                _annotation(ANNOTATION_SCHEMA, schemaDescription("${description}创建命令"))
            }
        }.writeTo(viewDir)

        val updateCommand = "${domainObjectClassName}UpdateCommand"
        _java("$updateCommand.java") {
            _package(viewPackage)
            _class(updateCommand) {
                _public()
                _annotation(ANNOTATION_DATA)
                _annotation(ANNOTATION_SCHEMA, schemaDescription("${description}更新命令"))
                _field(PARAM_ID) {
                    _protected()
                    _type(TYPE_STRING)
                    _annotation(ANNOTATION_SCHEMA, schemaDescription("${description}ID"))
                }
            }
        }.writeTo(viewDir)

        val deleteCommand = "${domainObjectClassName}DeleteCommand"
        _java("$deleteCommand.java") {
            _package(viewPackage)
            _class(deleteCommand) {
                _public()
                _annotation(ANNOTATION_DATA)
                _annotation(ANNOTATION_SCHEMA, schemaDescription("${description}删除命令"))
                _field(PARAM_ID) {
                    _protected()
                    _type(TYPE_STRING)
                    _annotation(ANNOTATION_SCHEMA, schemaDescription("${description}ID"))
                }
            }
        }.writeTo(viewDir)

        val vo = "${domainObjectClassName}VO"
        _java("$vo.java") {
            _package(viewPackage)
            _class(vo) {
                _public()
                _annotation(ANNOTATION_DATA)
                _annotation(ANNOTATION_SCHEMA, schemaDescription("${description}视图"))
                _field(PARAM_ID) {
                    _protected()
                    _type(TYPE_STRING)
                    _annotation(ANNOTATION_SCHEMA, schemaDescription("${description}ID"))
                }
            }
        }.writeTo(viewDir)

        val query = "${domainObjectClassName}Query"
        _java("$query.java") {
            _package(viewPackage)
            _class(query) {
                _public()
                _annotation(ANNOTATION_DATA)
                _annotation(ANNOTATION_SCHEMA, schemaDescription("${description}查询条件"))
            }
        }.writeTo(viewDir)

        val idGenerator = "${domainObjectClassName}IdGenerator"
        _java("$idGenerator.java") {
            _package(modulePackage)
            _interface(idGenerator) {
                _public()
                _comment("${description}ID生成器")
                _extends(TYPE_DOMAIN_ID_GENERATOR, "$viewPackage.$createCommand")

            }
        }.writeTo(dir)

        val facadeAdapter = "${domainObjectClassName}FacadeAdapter"
        val facadeAdapterParam = facadeAdapter.lowercaseFirst()
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

        val facadeAdapterImpl = "${facadeAdapter}Impl"
        _java("$facadeAdapterImpl.java") {
            _package(modulePackage)
            _class(facadeAdapterImpl) {
                _public()
                _comment("领域模型/视图 转换适配器实现")
                _annotation(ANNOTATION_COMPONENT)

                _implements(facadeAdapter)

                _field(idGenerator.lowercaseFirst()) {
                    _protected()
                    _annotation(ANNOTATION_AUTOWIRED)
                    _type(idGenerator)
                }

                _field("validator") {
                    _protected()
                    _annotation(ANNOTATION_AUTOWIRED)
                    _type(TYPE_DOMAIN_VALIDATOR)
                }

                _method("from") {
                    _override()
                    _public()
                    _return(domainObjectClass)
                    _param("$viewPackage.$createCommand", "create")
                    _todo()
                }

                _method("from") {
                    _override()
                    _public()
                    _return(domainObjectClass)
                    _param("$viewPackage.$updateCommand", "update")
                    _param(domainObjectClass, "old")
                    _todo()
                }

                _method("do2vo") {
                    _override()
                    _public()
                    _return("$viewPackage.$vo")
                    _param(domainObjectClass, domainObjectParam)
                    _todo()
                }

                _method("toConditions") {
                    _override()
                    _public()
                    _return(TYPE_DOMAIN_CONDITIONS)
                    _param("$viewPackage.$query", "query")
                    _body("return new ${TYPE_DOMAIN_LAMBDA_CONDITIONS.toSampleName()}();")
                    _import(TYPE_DOMAIN_LAMBDA_CONDITIONS)
                }
            }
        }.writeTo(dir)

        val searcher = "${domainObjectClassName}Searcher"
        _java("$searcher.java") {
            _package(modulePackage)
            _interface(searcher) {
                _public()
                _comment("搜索")

                _method("get") {
                    _return("$viewPackage.$vo")
                    _param(TYPE_STRING, PARAM_ID)
                    _comment("根据ID获得视图")
                }

                _method("list") {
                    _return("List<$vo>", false)
                    _import(TYPE_LIST)
                    _param("$viewPackage.$query", "query")
                    _comment("列表查询")
                }

                _method("page") {
                    _return("Pages<$vo>", false)
                    _import(TYPE_PAGES)
                    _param("$viewPackage.$query", "query")
                    _param("Pages.Args", "page", false)
                    _comment("分页查询")
                }
            }
        }.writeTo(dir)

        val searcherImpl = "${searcher}Impl"
        _java("$searcherImpl.java") {
            _package(modulePackage)
            _class(searcherImpl) {
                _public()
                _annotation(ANNOTATION_COMPONENT)
                _comment("搜索实现")
                _implements(searcher)

                _field(facadeAdapterParam) {
                    _protected()
                    _annotation(ANNOTATION_AUTOWIRED)
                    _type(facadeAdapter)
                }

                _field(domainRepositoryParam) {
                    _protected()
                    _annotation(ANNOTATION_AUTOWIRED)
                    _type(domainRepositoryClass)
                }

                _import(domainObjectClass)

                _method("get") {
                    _override()
                    _public()
                    _return("$viewPackage.$vo")
                    _param(TYPE_STRING, PARAM_ID)
                    _body(
                        "$domainObjectClassName $domainObjectParam=$domainRepositoryParam.get(id);",
                        "if ($domainObjectParam == null) {return null;}",
                        "return $facadeAdapterParam.do2vo($domainObjectParam);"
                    )
                }

                _method("list") {
                    _override()
                    _public()
                    _return("List<$vo>", false)
                    _import(TYPE_LIST)
                    _param("$viewPackage.$query", "query")
                    _body(
                        "return $domainRepositoryParam.select($facadeAdapterParam.toConditions(query))",
                        ".list()",
                        ".stream()",
                        ".map($facadeAdapterParam::do2vo)",
                        ".collect(Collectors.toList());"
                    )
                    _import(TYPE_COLLECTORS)
                }

                _method("page") {
                    _override()
                    _public()
                    _return("Pages<$vo>", false)
                    _import(TYPE_PAGES)
                    _param("$viewPackage.$query", "query")
                    _param("Pages.Args", "page", false)
                    _body(
                        "return $domainRepositoryParam",
                        ".page($facadeAdapterParam.toConditions(query), page)",
                        ".map($facadeAdapterParam::do2vo);"
                    )
                }
            }
        }.writeTo(dir)

        val applicationService = "${domainObjectClassName}ApplicationService"
        _java("$applicationService.java") {
            _package(modulePackage)
            _class(applicationService) {
                _public()
                _comment("应用服务")
                _annotation(ANNOTATION_SERVICE)

                _import(domainObjectClass)

                _field(domainServiceParam) {
                    _protected()
                    _annotation(ANNOTATION_AUTOWIRED)
                    _type(domainServiceClass)
                }

                _field(facadeAdapterParam) {
                    _protected()
                    _annotation(ANNOTATION_AUTOWIRED)
                    _type(facadeAdapter)
                }

                _field(domainRepositoryParam) {
                    _protected()
                    _annotation(ANNOTATION_AUTOWIRED)
                    _type(domainRepositoryClass)
                }

                _method("create") {
                    _public()
                    _return(TYPE_VOID)
                    _param("$viewPackage.$createCommand", "create")
                    _param(userClass, "user")
                    _body(
                        "$domainObjectClassName $domainObjectParam=$facadeAdapterParam.from(create);",
                        "$domainServiceParam.create($domainObjectParam,user);"
                    )
                }

                _method("update") {
                    _public()
                    _return(TYPE_VOID)
                    _param("$viewPackage.$updateCommand", "update")
                    _param(userClass, "user")
                    _body(
                        "$domainObjectClassName old$domainObjectClassName=$domainRepositoryParam.get(update.getId());",
                        "if(old$domainObjectClassName == null) {",
                        "throw new DomainNotFoundException($domainObjectClassName.class, update.getId());}",
                        "$domainObjectClassName new$domainObjectClassName=$facadeAdapterParam.from(update,old$domainObjectClassName);",
                        "$domainServiceParam.update(new$domainObjectClassName,old$domainObjectClassName,user);"
                    )
                    _import(TYPE_DOMAIN_NOT_FOUND_EXCEPTION)
                }

                _method("delete") {
                    _public()
                    _return(TYPE_VOID)
                    _param("$viewPackage.$deleteCommand", "delete")
                    _param(userClass, "user")
                    _body(
                        "$domainObjectClassName $domainObjectParam=$domainRepositoryParam.get(delete.getId());",
                        "if($domainObjectParam == null) {",
                        "throw new DomainNotFoundException($domainObjectClassName.class, delete.getId());}",
                        "$domainServiceParam.delete($domainObjectParam,user);"
                    )
                    _import(TYPE_DOMAIN_NOT_FOUND_EXCEPTION)
                }
            }
        }.writeTo(dir)



        if (model.myBatisPlus.get()) {

        }
    }
}