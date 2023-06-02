package com.github.linyuzai.cloud.plugin.intellij.module

import com.github.linyuzai.cloud.plugin.intellij.builder.*
import java.io.File

object ModuleFileGenerator {

    @JvmStatic
    fun generate(model: ModuleModel, dir: File) {
        val userClass = model.userClass.get()
        val loginClass = model.loginAnnotationClass.get()
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

        val createCommandClassName = "${domainObjectClassName}CreateCommand"
        _java("$createCommandClassName.java") {
            _package(viewPackage)
            _class(createCommandClassName) {
                _public()
                _annotation(ANNOTATION_DATA)
                _annotation(ANNOTATION_SCHEMA, schemaDescription("${description}创建命令"))
            }
        }.writeTo(viewDir)

        val updateCommandClassName = "${domainObjectClassName}UpdateCommand"
        _java("$updateCommandClassName.java") {
            _package(viewPackage)
            _class(updateCommandClassName) {
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

        val deleteCommandClassName = "${domainObjectClassName}DeleteCommand"
        _java("$deleteCommandClassName.java") {
            _package(viewPackage)
            _class(deleteCommandClassName) {
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

        val voClassName = "${domainObjectClassName}VO"
        _java("$voClassName.java") {
            _package(viewPackage)
            _class(voClassName) {
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

        val queryClassName = "${domainObjectClassName}Query"
        _java("$queryClassName.java") {
            _package(viewPackage)
            _class(queryClassName) {
                _public()
                _annotation(ANNOTATION_DATA)
                _annotation(ANNOTATION_SCHEMA, schemaDescription("${description}查询条件"))
            }
        }.writeTo(viewDir)

        val idGeneratorClassName = "${domainObjectClassName}IdGenerator"
        _java("$idGeneratorClassName.java") {
            _package(modulePackage)
            _interface(idGeneratorClassName) {
                _public()
                _comment("${description}ID生成器")
                _extends(TYPE_DOMAIN_ID_GENERATOR, "$viewPackage.$createCommandClassName")

            }
        }.writeTo(dir)

        val facadeAdapterClassName = "${domainObjectClassName}FacadeAdapter"
        val facadeAdapterParam = facadeAdapterClassName.lowercaseFirst()
        _java("$facadeAdapterClassName.java") {
            _package(modulePackage)
            _interface(facadeAdapterClassName) {
                _public()
                _comment("领域模型/视图 转换适配器")

                _method("from") {
                    _return(domainObjectClass)
                    _param("create") {
                        _type("$viewPackage.$createCommandClassName")
                    }
                    _comment("创建命令视图转领域模型")
                }

                _method("from") {
                    _return(domainObjectClass)
                    _param("update") {
                        _type("$viewPackage.$updateCommandClassName")
                    }
                    _param("old") {
                        _type(domainObjectClass)
                    }
                    _comment("更新命令视图转领域模型")
                }

                _method("do2vo") {
                    _return("$viewPackage.$voClassName")
                    _param(domainObjectParam) {
                        _type(domainObjectClass)
                    }
                    _comment("领域模型转视图")
                }

                _method("toConditions") {
                    _return(TYPE_DOMAIN_CONDITIONS)
                    _param("query") {
                        _type("$viewPackage.$queryClassName")
                    }
                    _comment("查询转条件")
                }
            }
        }.writeTo(dir)

        val facadeAdapterImplClassName = "${facadeAdapterClassName}Impl"
        _java("$facadeAdapterImplClassName.java") {
            _package(modulePackage)
            _class(facadeAdapterImplClassName) {
                _public()
                _comment("领域模型/视图 转换适配器实现")
                _annotation(ANNOTATION_COMPONENT)

                _implements(facadeAdapterClassName)

                _field(idGeneratorClassName.lowercaseFirst()) {
                    _protected()
                    _annotation(ANNOTATION_AUTOWIRED)
                    _type(idGeneratorClassName)
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
                    _param("create") {
                        _type("$viewPackage.$createCommandClassName")
                    }
                    _todo()
                }

                _method("from") {
                    _override()
                    _public()
                    _return(domainObjectClass)
                    _param("update") {
                        _type("$viewPackage.$updateCommandClassName")
                    }
                    _param("old") {
                        _type(domainObjectClass)
                    }
                    _todo()
                }

                _method("do2vo") {
                    _override()
                    _public()
                    _return("$viewPackage.$voClassName")
                    _param(domainObjectParam) {
                        _type(domainObjectClass)
                    }
                    _todo()
                }

                _method("toConditions") {
                    _override()
                    _public()
                    _return(TYPE_DOMAIN_CONDITIONS)
                    _param("query") {
                        _type("$viewPackage.$queryClassName")
                    }
                    _body("return new ${TYPE_DOMAIN_LAMBDA_CONDITIONS.toSampleName()}();")
                    _import(TYPE_DOMAIN_LAMBDA_CONDITIONS)
                }
            }
        }.writeTo(dir)

        val searcherClassName = "${domainObjectClassName}Searcher"
        val searcherParam = searcherClassName.lowercaseFirst()
        _java("$searcherClassName.java") {
            _package(modulePackage)
            _interface(searcherClassName) {
                _public()
                _comment("搜索")

                _method("get") {
                    _return("$viewPackage.$voClassName")
                    _param(PARAM_ID) {
                        _type(TYPE_STRING)
                    }
                    _comment("根据ID获得视图")
                }

                _method("list") {
                    _return("List<$voClassName>", false)
                    _import(TYPE_LIST)
                    _param("query") {
                        _type("$viewPackage.$queryClassName")
                    }
                    _comment("列表查询")
                }

                _method("page") {
                    _return("Pages<$voClassName>", false)
                    _import(TYPE_PAGES)
                    _param("query") {
                        _type("$viewPackage.$queryClassName")
                    }
                    _param("page") {
                        _type("Pages.Args", false)
                    }
                    _comment("分页查询")
                }
            }
        }.writeTo(dir)

        val searcherImplClassName = "${searcherClassName}Impl"
        _java("$searcherImplClassName.java") {
            _package(modulePackage)
            _class(searcherImplClassName) {
                _public()
                _annotation(ANNOTATION_COMPONENT)
                _comment("搜索实现")
                _implements(searcherClassName)

                _field(facadeAdapterParam) {
                    _protected()
                    _annotation(ANNOTATION_AUTOWIRED)
                    _type(facadeAdapterClassName)
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
                    _return("$viewPackage.$voClassName")
                    _param(PARAM_ID) {
                        _type(TYPE_STRING)
                    }
                    _body(
                        "$domainObjectClassName $domainObjectParam=$domainRepositoryParam.get(id);",
                        "if ($domainObjectParam == null) {return null;}",
                        "return $facadeAdapterParam.do2vo($domainObjectParam);"
                    )
                }

                _method("list") {
                    _override()
                    _public()
                    _return("List<$voClassName>", false)
                    _import(TYPE_LIST)
                    _param("query") {
                        _type("$viewPackage.$queryClassName")
                    }
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
                    _return("Pages<$voClassName>", false)
                    _import(TYPE_PAGES)
                    _param("query") {
                        _type("$viewPackage.$queryClassName")
                    }
                    _param("page") {
                        _type("Pages.Args", false)
                    }
                    _body(
                        "return $domainRepositoryParam",
                        ".page($facadeAdapterParam.toConditions(query), page)",
                        ".map($facadeAdapterParam::do2vo);"
                    )
                }
            }
        }.writeTo(dir)

        val applicationServiceClassName = "${domainObjectClassName}ApplicationService"
        val applicationServiceParam = applicationServiceClassName.lowercaseFirst()
        _java("$applicationServiceClassName.java") {
            _package(modulePackage)
            _class(applicationServiceClassName) {
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
                    _type(facadeAdapterClassName)
                }

                _field(domainRepositoryParam) {
                    _protected()
                    _annotation(ANNOTATION_AUTOWIRED)
                    _type(domainRepositoryClass)
                }

                _method("create") {
                    _public()
                    _param("create") {
                        _type("$viewPackage.$createCommandClassName")
                    }
                    _param("user") {
                        _type(userClass)
                    }
                    _body(
                        "$domainObjectClassName $domainObjectParam=$facadeAdapterParam.from(create);",
                        "$domainServiceParam.create($domainObjectParam,user);"
                    )
                }

                _method("update") {
                    _public()
                    _param("update") {
                        _type("$viewPackage.$updateCommandClassName")
                    }
                    _param("user") {
                        _type(userClass)
                    }
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
                    _param("delete") {
                        _type("$viewPackage.$deleteCommandClassName")
                    }
                    _param("user") {
                        _type(userClass)
                    }
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

        val controllerClassName = "${domainObjectClassName}Controller"
        _java("$controllerClassName.java") {
            _package(modulePackage)
            _class(controllerClassName) {
                _public()

                _annotation(ANNOTATION_TAG, tagName(description))
                _annotation(ANNOTATION_REST_CONTROLLER)
                _annotation(ANNOTATION_REQUEST_MAPPING, annotationValue(domainObjectParam))

                _field(applicationServiceParam) {
                    _protected()
                    _annotation(ANNOTATION_AUTOWIRED)
                    _type(applicationServiceClassName)
                }

                _field(searcherParam) {
                    _protected()
                    _annotation(ANNOTATION_AUTOWIRED)
                    _type(searcherClassName)
                }

                _method("create") {
                    _public()
                    _annotation(ANNOTATION_OPERATION, operationSummary("创建$description"))
                    _annotation(ANNOTATION_POST_MAPPING)
                    _param("create") {
                        _annotation(ANNOTATION_REQUEST_BODY)
                        _type("$viewPackage.$createCommandClassName")
                    }
                    _param("user") {
                        _annotation(loginClass)
                        _type(userClass)
                    }
                    _body("$applicationServiceParam.create(create, user);")
                }

                _method("update") {
                    _public()
                    _annotation(ANNOTATION_OPERATION, operationSummary("更新$description"))
                    _annotation(ANNOTATION_PUT_MAPPING)
                    _param("update") {
                        _annotation(ANNOTATION_REQUEST_BODY)
                        _type("$viewPackage.$updateCommandClassName")
                    }
                    _param("user") {
                        _annotation(loginClass)
                        _type(userClass)
                    }
                    _body("$applicationServiceParam.update(update, user);")
                }

                _method("delete") {
                    _public()
                    _annotation(ANNOTATION_OPERATION, operationSummary("删除$description"))
                    _annotation(ANNOTATION_DELETE_MAPPING)
                    _param("delete") {
                        _annotation(ANNOTATION_REQUEST_BODY)
                        _type("$viewPackage.$deleteCommandClassName")
                    }
                    _param("user") {
                        _annotation(loginClass)
                        _type(userClass)
                    }
                    _body("$applicationServiceParam.delete(delete, user);")
                }

                _method("get") {
                    _public()
                    _annotation(ANNOTATION_OPERATION, operationSummary("${description}详情"))
                    _annotation(ANNOTATION_GET_MAPPING, annotationValue("{id}"))
                    _return("$viewPackage.$voClassName")
                    _param("id") {
                        _annotation(ANNOTATION_PARAMETER, parameterDescription("${description}ID"))
                        _annotation(ANNOTATION_PATH_VARIABLE)
                        _type(TYPE_STRING)
                    }
                    _body("return $searcherParam.get(id);")
                }

                _method("list") {
                    _public()
                    _annotation(ANNOTATION_OPERATION, operationSummary("${description}列表"))
                    _annotation(ANNOTATION_GET_MAPPING, annotationValue("list"))
                    _return("List<$voClassName>", false)
                    _import(TYPE_LIST)
                    _param("query") {
                        _type("$viewPackage.$queryClassName")
                    }
                    _body("return $searcherParam.list(query);")
                }

                _method("page") {
                    _public()
                    _annotation(ANNOTATION_OPERATION, operationSummary("${description}分页"))
                    _annotation(ANNOTATION_GET_MAPPING, annotationValue("page"))
                    _return("Pages<$voClassName>", false)
                    _import(TYPE_PAGES)
                    _param("query") {
                        _type("$viewPackage.$queryClassName")
                    }
                    _param("page") {
                        _type("Pages.Args", false)
                    }
                    _body("return $searcherParam.page(query, page);")
                }
            }
        }.writeTo(dir)

        if (model.myBatisPlus.get()) {
            val mbpPackage = "$modulePackage.infrastructure.${domainObjectClass.uppercase()}.mbp"
            val mbpDir = File(dir, "infrastructure/${domainObjectClass.uppercase()}/mbp")

        }
    }
}