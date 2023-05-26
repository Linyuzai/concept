package com.github.linyuzai.cloud.plugin.intellij.domain

import com.github.linyuzai.cloud.plugin.intellij.builder._java
import java.io.File

object DomainFileGenerator {

    @JvmStatic
    fun generate(model: DomainModel, dir: File) {
        _java("${model.domainClassName.get()}.java") {
            _package(model.domainPackage.get())
            _interface(model.domainClassName.get()) {
                _extendsDomainEntity()
                model.domainProps.forEach {
                    _method(it.propName.get()) {
                        _return(it.propClass.get())
                        _comment(it.propComment.get())
                    }
                }
            }
        }.writeTo(dir)
    }
}