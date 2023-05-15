@file:JvmName("ConceptStarterSettings")
package com.github.linyuzai.cloud.plugin.intellij

import com.intellij.ide.starters.shared.LibraryLinkType

import com.intellij.openapi.projectRoots.JavaSdkVersion
import com.intellij.openapi.util.NlsContexts.DialogTitle
import com.intellij.openapi.util.NlsContexts.Label
import com.intellij.openapi.util.NlsSafe

data class ConceptStarterLanguage(
    val id: String,
    @NlsSafe val title: String,
    val languageId: String,
    val isBuiltIn: Boolean = false,
    @NlsSafe val description: String? = null
)

data class ConceptStarterTestRunner(
    val id: String,
    @NlsSafe val title: String
)

data class ConceptStarterProjectType(
    val id: String,
    @NlsSafe val title: String,
    @NlsSafe val description: String? = null
)

data class ConceptStarterAppType(
    val id: String,
    @NlsSafe val title: String
)

data class ConceptStarterAppPackaging(
    val id: String,
    @NlsSafe val title: String,
    @NlsSafe val description: String? = null
)

data class ConceptStarterLanguageLevel(
    val id: String,
    @NlsSafe val title: String,
    /**
     * Version string that can be parsed with [JavaSdkVersion.fromVersionString].
     */
    val javaVersion: String
)

class ConceptCustomizedMessages {
    var projectTypeLabel: @Label String? = null
    var serverUrlDialogTitle: @DialogTitle String? = null
    var dependenciesLabel: @Label String? = null
    var selectedDependenciesLabel: @Label String? = null
    var noDependenciesSelectedLabel: @Label String? = null
    var frameworkVersionLabel: @Label String? = null
}

class ConceptStarterWizardSettings(
    val projectTypes: List<ConceptStarterProjectType>,
    val languages: List<ConceptStarterLanguage>,
    val isExampleCodeProvided: Boolean,
    val isPackageNameEditable: Boolean,
    val languageLevels: List<ConceptStarterLanguageLevel>,
    val defaultLanguageLevel: ConceptStarterLanguageLevel?,
    val packagingTypes: List<ConceptStarterAppPackaging>,
    val applicationTypes: List<ConceptStarterAppType>,
    val testFrameworks: List<ConceptStarterTestRunner>,
    val customizedMessages: ConceptCustomizedMessages?
)

class ConceptPluginRecommendation(
    val pluginId: String,
    val dependencyIds: List<String>
) {
    constructor(pluginId: String, vararg dependencyIds: String) : this(pluginId, dependencyIds.toList())
}

interface ConceptLibraryInfo {
    @get:NlsSafe
    val title: String
    val description: String?
    val links: List<ConceptLibraryLink>
    val isRequired: Boolean
    val isDefault: Boolean
}

class ConceptLibraryLink(
    val type: LibraryLinkType,
    @NlsSafe
    val url: String,
    @NlsSafe
    val title: String? = null
)

const val DEFAULT_MODULE_NAME: String = "demo"
const val DEFAULT_MODULE_GROUP: String = "com.example"
const val DEFAULT_MODULE_ARTIFACT: String = "demo"
const val DEFAULT_MODULE_VERSION: String = "1.0-SNAPSHOT"
const val DEFAULT_PACKAGE_NAME: String = "$DEFAULT_MODULE_GROUP.$DEFAULT_MODULE_ARTIFACT"

val JAVA_STARTER_LANGUAGE: ConceptStarterLanguage = ConceptStarterLanguage("java", "Java", "JAVA", true)
val KOTLIN_STARTER_LANGUAGE: ConceptStarterLanguage = ConceptStarterLanguage("kotlin", "Kotlin", "kotlin")
val GROOVY_STARTER_LANGUAGE: ConceptStarterLanguage = ConceptStarterLanguage("groovy", "Groovy", "Groovy")

val MAVEN_PROJECT: ConceptStarterProjectType = ConceptStarterProjectType("maven", "Maven")
val GRADLE_PROJECT: ConceptStarterProjectType = ConceptStarterProjectType("gradle", "Gradle")

val JUNIT_TEST_RUNNER: ConceptStarterTestRunner = ConceptStarterTestRunner("junit", "JUnit")
val TESTNG_TEST_RUNNER: ConceptStarterTestRunner = ConceptStarterTestRunner("testng", "TestNG")