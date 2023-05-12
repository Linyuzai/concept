package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.ide.starters.shared.*
import com.intellij.openapi.util.UserDataHolderBase

class ConceptWebStarterContext : UserDataHolderBase() {
    var name: String = DEFAULT_MODULE_NAME
    var group: String = DEFAULT_MODULE_GROUP
    var artifact: String = DEFAULT_MODULE_ARTIFACT
    var version: String = DEFAULT_MODULE_VERSION

    var isCreatingNewProject: Boolean = false

    lateinit var serverUrl: String
    lateinit var serverOptions: ConceptWebStarterServerOptions

    lateinit var language: StarterLanguage

    var frameworkVersion: ConceptWebStarterFrameworkVersion? = null
    var projectType: StarterProjectType? = null
    var packageName: String = DEFAULT_PACKAGE_NAME
    var languageLevel: StarterLanguageLevel? = null
    var packaging: StarterAppPackaging? = null
    var applicationType: StarterAppType? = null
    var testFramework: StarterTestRunner? = null
    var includeExamples: Boolean = true

    val dependencies: MutableSet<ConceptWebStarterDependency> = HashSet()

    var result: ConceptDownloadResult? = null
}
